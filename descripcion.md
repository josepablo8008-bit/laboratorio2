El programa:
1. Solicita por consola:
   - **Pasajeros ya a bordo** (0 a 30).
   - **Nuevos pasajeros** que intentarán abordar.
2. Controla la **capacidad del bus** con un `Semaphore` (tokens = espacios disponibles).
3. Controla que **solo 3 pasajeros** puedan **subir al mismo tiempo** con otro `Semaphore`.
4. Usa un **bloqueo (`synchronized`)** para imprimir ordenadamente y actualizar contadores.
5. Cada pasajero se modela como un **hilo** que:
   - Intenta **reservar espacio** (`tryAcquire`).
   - Si logra reservar, **adquiere turno de subida**, simula validación y equipaje con un `sleep` aleatorio (2–5 s), y **confirma abordaje**.
   - Si no hay espacio, es **rechazado**.

**Constantes clave:**
- `CAPACIDAD_BUS = 30`
- `MAXIMO_SUBIENDO = 3`
