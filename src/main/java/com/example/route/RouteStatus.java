package com.example.route;

public enum RouteStatus {
    PENDING,      // Ruta a fost creată, dar nu a început încă
    SCHEDULED,    // Programată pentru o anumită oră
    IN_PROGRESS,  // Vehiculul sau curierul este pe traseu
    PAUSED,       // Oprire temporară pe traseu
    DELAYED,      // Există întârzieri (trafic, condiții meteo etc.)
    COMPLETED,    // Ruta a fost finalizată cu succes
    CANCELLED,     // Ruta a fost anulată
    PLANNED
}
