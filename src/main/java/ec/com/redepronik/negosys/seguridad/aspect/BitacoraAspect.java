package ec.com.redepronik.negosys.seguridad.aspect;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ec.com.redepronik.negosys.rrhh.service.PersonaService;
import ec.com.redepronik.negosys.seguridad.dao.BitacoraDao;
import ec.com.redepronik.negosys.seguridad.entity.Bitacora;

@Component
@Aspect
public class BitacoraAspect implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	public BitacoraDao bitacoraDao;

	@Autowired
	public PersonaService personaService;

	@After("execution(public * ec.com.redepronik.invfac.service..*.eliminar(..)) "
			+ "|| execution(public * ec.com.redepronik.rrhh.service..*.eliminar(..)) ")
	public void auditarEliminarNegosys(JoinPoint joinPoint) {
		Object obj = (joinPoint.getArgs())[0];
		String mensaje = "";
		try {
			String nombreObjeto = obj.getClass().getSimpleName();
			Method metodo = obj.getClass().getMethod(
					"get" + nombreObjeto + "id", new Class[] {});
			Method metodoEliminar = obj.getClass().getMethod("getActivo",
					new Class[] {});

			if ((Boolean) metodoEliminar.invoke(obj, new Object[] {}))
				mensaje = "Se Activó el objeto " + nombreObjeto
						+ " con la llave: # "
						+ metodo.invoke(obj, new Object[] {});
			else
				mensaje = "Se Desactivó el objeto " + nombreObjeto
						+ " con la llave: # "
						+ metodo.invoke(obj, new Object[] {});

		} catch (Exception e) {
			mensaje = "error al leer método";
		}
		bitacoraDao.insertar(new Bitacora(
				new Timestamp((new Date()).getTime()), mensaje, personaService
						.obtenerPorCedula(SecurityContextHolder.getContext()
								.getAuthentication().getName())));
	}

	@After("execution(public * ec.com.redepronik.invfac.service..*.insertar(..)) "
			+ "|| execution(public * ec.com.redepronik.invfac.service..*.actualizar(..)) "
			+ "|| execution(public * ec.com.redepronik.rrhh.service..*.insertar(..)) "
			+ "|| execution(public * ec.com.redepronik.rrhh.service..*.actualizar(..)) ")
	public void auditarNegosys(JoinPoint joinPoint) {
		Object obj = (joinPoint.getArgs())[0];
		String mensaje = "";
		try {
			String nombreObjeto = obj.getClass().getSimpleName();
			Method metodo = obj.getClass().getMethod(
					"get" + nombreObjeto + "id", new Class[] {});
			mensaje = "Se acaba de " + joinPoint.getSignature().getName()
					+ " el objeto " + nombreObjeto + " con la llave: # "
					+ metodo.invoke(obj, new Object[] {});
		} catch (Exception e) {
			mensaje = "error al leer método";
		}
		bitacoraDao.insertar(new Bitacora(
				new Timestamp((new Date()).getTime()), mensaje, personaService
						.obtenerPorCedula(SecurityContextHolder.getContext()
								.getAuthentication().getName())));
	}

	@After("execution(public * ec.com.redepronik.negosys.seguridad.service.MenuService.obtenerPorUsuario(..)) ")
	public void ingresoNegosys(JoinPoint joinPoint) {
		bitacoraDao.insertar(new Bitacora(
				new Timestamp((new Date()).getTime()), "Ingresó al Sistema",
				personaService.obtenerActivoPorCedula(SecurityContextHolder
						.getContext().getAuthentication().getName())));
	}
}