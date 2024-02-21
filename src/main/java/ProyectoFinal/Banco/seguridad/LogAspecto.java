package ProyectoFinal.Banco.seguridad;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	
	// Registrar en el fichero log despues de lanzar cualquier excepción	
	// En estos caso el pointcut es cualquier metodo o clase de la app
	@AfterThrowing(pointcut = "execution(* ProyectoFinal.Banco..*.*(..))", throwing = "ex")
	public void logException(JoinPoint joinPoint, Throwable ex) {
		logger.error("Error en el método {}() de la clase {}: {}", 
				joinPoint.getSignature().getName(),
				joinPoint.getTarget().getClass().getName(),
				ex.getMessage() + "\n" + ex.getStackTrace());
	}
	
	// Registrar en el fichero log la entrada a métodos
    @Before("execution(* ProyectoFinal.Banco..*.*(..))")
    public void logMetodoEntrada(JoinPoint joinPoint) {
        logger.info("Entrando en el método {}() de la clase {}", 
                     joinPoint.getSignature().getName(),
                     joinPoint.getTarget().getClass().getName());
    }

    // Registrar en el fichero log la salida de métodos (después de la ejecución exitosa)
    @AfterReturning("execution(* ProyectoFinal.Banco..*.*(..))")
    public void logMethodoSalida(JoinPoint joinPoint) {
        logger.info("Saliendo del método {}() de la clase {}", 
                     joinPoint.getSignature().getName(),
                     joinPoint.getTarget().getClass().getName());
    }

}