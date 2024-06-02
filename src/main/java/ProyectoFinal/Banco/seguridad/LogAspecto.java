package ProyectoFinal.Banco.seguridad;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *  Aspecto para realizar un seguimiento de las acciones en la app en fichero log. 
 *  La programación orientada aspectos (AOP) permite modularizar el código para 
 *  encapsular tareas repetitivas transversales separandolas de la lógica de negocio. 
 *  Para este caso de control del log se utilizará la librería SLF4J.
 */
@Aspect
@Component
public class LogAspecto {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            return "SinSesion";
        }
    }
	
	// Registrar en el fichero log después de lanzar cualquier excepción
    // En estos casos el pointcut es cualquier método o clase de la app
    @AfterThrowing(pointcut = "execution(* ProyectoFinal.Banco..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String user = getCurrentUser();
        logger.error("Usuario: {} - Error en el método {}() de la clase {}: {}", 
                user,
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getName(),
                ex.getMessage(), ex);
    }

    // Registrar en el fichero log la entrada a métodos
    @Before("execution(* ProyectoFinal.Banco..*.*(..))")
    public void logMetodoEntrada(JoinPoint joinPoint) {
        String user = getCurrentUser();
        logger.info("Usuario: {} - Entrando en el método {}() de la clase {}", 
                     user,
                     joinPoint.getSignature().getName(),
                     joinPoint.getTarget().getClass().getName());
    }

    // Registrar en el fichero log la salida de métodos (después de la ejecución exitosa)
    @AfterReturning("execution(* ProyectoFinal.Banco..*.*(..))")
    public void logMethodoSalida(JoinPoint joinPoint) {
        String user = getCurrentUser();
        logger.info("Usuario: {} - Saliendo del método {}() de la clase {}", 
                     user,
                     joinPoint.getSignature().getName(),
                     joinPoint.getTarget().getClass().getName());
    }
}