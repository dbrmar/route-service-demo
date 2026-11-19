package com.example.route;

public record RouteResponse(Long id, String name, RouteStatus status) {
    static RouteResponse from(Route route) {
        return new RouteResponse(route.getId(), route.getName(), route.getStatus());
    }
}
