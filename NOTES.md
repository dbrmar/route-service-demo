# Study notes: steps 1 to 4

## Step 1: data layer
- **Entity** (`@Entity`): a class mapped to a table. `@Id` is the primary key, and `@GeneratedValue(IDENTITY)` means the database generates the id.
- **No-arg constructor**: Hibernate builds an empty object by reflection, then fills its fields from the row. It is `protected`, not `private`, because Hibernate's lazy-loading proxies are subclasses and must call it.
- **`@Enumerated(EnumType.STRING)`** stores `"PLANNED"`. The default, ORDINAL, stores the position number, so reordering the enum silently corrupts old rows.
- **Repository**: an interface extending `JpaRepository`. Spring generates the implementation at startup.
- **Three ways to make a query**: a derived method name (`findByStatus`, parsed into JPQL at startup, so a typo fails at startup), `@Query` with JPQL or native SQL, or the `EntityManager` directly.
- **DTO records** prevent mass assignment (a client setting `id` or `status`), decouple the JSON from the table, avoid lazy-loading and recursion problems, and hold the validation rules.
- **`ddl-auto`**: `create-drop` is the default for embedded H2. In production, use `validate` or `none` and manage the schema with Flyway or Liquibase.

## Step 2: service
- **Service**: holds the business logic between the controller and the repository, and is where transactions start.
- **Constructor injection**: dependencies can be `final`, a missing bean fails at startup, tests are just `new Service(mock)`, and every dependency is visible. Field injection hides circular dependencies (it doesn't cause them); constructors make them fail fast.
- **`@Transactional`**: the proxy opens a transaction, commits on success, and rolls back on a **RuntimeException** (not a checked exception by default). A method-level annotation overrides the class-level one.
- **`readOnly = true`**: not about rollback. It gives the method one connection and one session (so lazy loading works and reads are consistent), disables flushing and dirty checking, and can route to a read replica.
- **Dirty checking**: a managed entity changed inside a writable transaction is UPDATEd at flush without calling `save()`. This does not happen for detached entities or in a read-only transaction.
- **Propagation**: `REQUIRED` (the default) joins the existing transaction or starts one. `REQUIRES_NEW` suspends the current one and starts its own (for example, for an audit log).
- **Self-invocation trap**: `this.method()` bypasses the proxy, so that method's `@Transactional` (including REQUIRES_NEW) is ignored. The fix is to move the method to another bean, or use `TransactionTemplate`.
- **Unchecked exception**: it rolls back, and it doesn't force a `throws` clause on every layer, so it can be handled centrally.

## Step 3: web layer
- **Path, query, body**: `@PathVariable` comes from `/routes/5`, `@RequestParam` from `?status=PLANNED`, and `@RequestBody` from the JSON body. Use the path for identity, the query for filtering, and the body for data.
- **`@Valid`**: Jackson builds the record first, then validation runs. On failure, `MethodArgumentNotValidException` is thrown, the method never runs, and the client gets a 400.
- **`ResponseEntity`**: needed for **201 Created** with a `Location` header. A plain return value is always 200.
- **`Location` with 201** is a header holding the new resource's URL. It is **not a redirect** (redirects are 3xx).
- **404** comes from your own code: the service throws, and `@RestControllerAdvice` maps the exception to a 404. Without the handler, the client gets a 500.
- **`ProblemDetail`** (RFC 9457): `type`, `title`, `status`, `detail` and `instance`, sent as `application/problem+json`. It gives every error the correct status and one standard shape, in one central place.

## Step 4: running it (real results)
- POST returned 201 with `Location: /routes/1`. GET list, filter and by id returned 200. `GET /routes/99` returned a 404 problem+json. A blank name returned a 400 with Spring's **default** error JSON, not a ProblemDetail.
- **SQL seen**: `create table` (from ddl-auto), then `insert ... values (?,?,default)`, then selects for findAll, findByStatus and findById. The `?` marks are bind parameters, which prevent SQL injection. The blank name produced **no SQL**.
- **IDENTITY vs SEQUENCE**: IDENTITY must INSERT immediately to learn the id, so there is no write-behind and no insert batching. SEQUENCE fetches ids ahead of time in blocks of 50, so INSERTs can wait until flush and be batched (the cost is gaps in the ids).
- **First-level cache**: the persistence context is per transaction. The same id twice in one transaction gives one SELECT and the same object. Across requests it runs again, unless a second-level cache is configured.

## Weak spots to revise
- `@RequestParam` comes from the query string, not the body.
- 404 is not automatic for a missing entity.
- 201 with `Location` is not a redirect.
- What `readOnly` is actually for.
- The self-invocation proxy trap.
- ORDINAL vs STRING for enums.
- `ddl-auto` per environment.
- IDENTITY vs SEQUENCE.
