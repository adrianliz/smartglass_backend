# 🤖 Smartglass Backend
[![CI pipeline status](https://github.com/adrianliz/smartglass-backend/workflows/CI/badge.svg)](https://github.com/adrianliz/smartglass-backend/actions)

## ℹ️Introducción

Smartglass pretende ser un software que permita a sus usuarios tener información en tiempo real del estado de sus
distintas máquinas-herramienta de corte de vidrio

También, se podrán generar distintas estadísticas derivadas de los resultados de explotación de dichas máquinas

## 💡 Estado

```diff
+ [En desarrollo]
```

## ⚡ Versiones disponibles

- :white_check_mark: v0.1.0 → Lectura de los eventos producidos de una base de datos MongoDB
- :white_check_mark: v0.2.0 → REST endpoint de consulta de los eventos producidos + Swagger (documentación API)
- :white_check_mark: v0.3.0 → División en módulos, REST endpoint para los gemelos digitales y cálculo de la
  disponibilidad para un periodo temporal
- :white_check_mark: v0.4.0 → Primera versión de los cálculos de los distintos ratios para un periodo temporal
- :white_check_mark: v0.5.0 → Cálculo de estadísticas y ratios
- :white_check_mark: v0.6.0 → BREAKING CHANGE: Simplificación del cálculo de las estadísticas asociadas a los estados
  de cada gemelo, persistiendo los estados por los que estos transitan y consultándolos cuando es necesario, en vez de
  mantener en memoria dichos estados y volver a calcularlos en cada reinicio del servidor

## 🏁 Integración continua

- Se dispone de un workflow de GitHub Actions que permite ejecutar los test unitarios y subir una imagen de la aplicación
  a Docker Hub en cada PUSH o PULL_REQUEST en la rama main

- 📋 Makefile:
  - Ejecutar `make build` para crear el artefacto maven
  - Ejecutar `make build` para ejecutar los tests unitarios
  - Ejecutar `make docker-up-remote` para crear un contenedor en la máquina 155.210.68.101 con la última imagen disponible
    en Docker Hub
  - Ejecutar `make docker-down-remote` para parar dicho contenedor 
  