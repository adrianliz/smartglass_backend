# 🤖 Smartglass Backend
![CI pipeline status](https://github.com/adrianliz/smartglass_backend/actions/workflows/ci.yml/badge.svg)

## ℹ️Introducción

Smartglass es un software que permite a sus usuarios el mantenimiento predictivo de sus máquinas-herramienta de mecanizado de vidrio a tavés de diferentes analíticas referentes a su funcionamiento productivo.

Este proyecto es el resultado de la implementación de la [metodología propuesta por dos investigadores de la Universidad de Auckland](https://doi.org/10.1016/j.jmsy.2018.05.003) para la implementación práctica de digital twins en el contexto de la Industria 4.0.

Forma parte de la realización de mi TFG para la Universidad de Zaragoza.

## 💡 Estado

```diff
- [Finalizado]
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
- :white_check_mark: v0.7.0 → Tests unitarios y añadido soporte para CI (integración continua)
- :white_check_mark: v0.8.0 → Integración con la ontología del gemelo digital

## 📁 Variables de entorno

Se DEBE crear un fichero .env en la carpeta `resources` que contenga las variables de entorno:
  - MONGO_URI = URI de la conexión a la base de datos de Mongo
  - TRANSITIONS_FILE = Ruta absoluta al fichero .json que define las transiciones
  - TWINS_UPDATE_DELAY = Delay en ms para actualizar la información asociada a cada gemelo digital

## 🏁 Integración continua

- Se dispone de un workflow de GitHub Actions que permite ejecutar los test unitarios y subir una imagen de la aplicación
  a Docker Hub en cada PUSH o PULL_REQUEST en la rama main

- 📋 Makefile:
  - Ejecutar `make docker-up-remote` para crear un contenedor en la máquina 155.210.68.101 con la última imagen disponible
    en Docker Hub
  - Ejecutar `make docker-down-remote` para parar dicho contenedor 
  - Ejecutar `make logs` para obtener los logs del contenedor en el fichero `logs.txt`

  - NOTA: Se necesita tener un usuario en la máquina 155 o modificar el servidor de producción.
  
