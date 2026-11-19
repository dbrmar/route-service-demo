package com.example.route;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RouteService {
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Transactional
    public RouteResponse create(CreateRouteRequest request) {
        Route route = routeRepository.save(new Route(request.name()));
        return RouteResponse.from(route);
    }

    public RouteResponse get(Long id) {
        return routeRepository.findById(id)
                .map(RouteResponse::from)
                .orElseThrow(() -> new RouteNotFoundException(id));
    }

    public List<RouteResponse> list(RouteStatus status) {
        if (status ==  null) {
            return routeRepository.findAll().stream().map(RouteResponse::from).toList();
        } else {
            return routeRepository.findByStatus(status).stream().map(RouteResponse::from).toList();
        }
    }
}
