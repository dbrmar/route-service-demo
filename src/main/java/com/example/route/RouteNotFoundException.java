package com.example.route;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(Long id) {
        super("Route with id " + id + " not found");
    }
}
