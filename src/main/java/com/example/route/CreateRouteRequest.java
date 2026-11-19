package com.example.route;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRouteRequest(@NotBlank @Size(max = 100) String name) {
}
