package com.example.route;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public List<RouteResponse> list(@RequestParam(required = false) RouteStatus status) {
        return routeService.list(status);                         // 200
    }

    @GetMapping("/{id}")
    public RouteResponse get(@PathVariable Long id) {
        return routeService.get(id);                         // 200
    }

    @PostMapping
    public ResponseEntity<RouteResponse> create(@RequestBody @Valid CreateRouteRequest request) {
        RouteResponse routeResponse = routeService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(routeResponse.id()).toUri();

        return ResponseEntity.created(location).body(routeResponse);
    }

}
