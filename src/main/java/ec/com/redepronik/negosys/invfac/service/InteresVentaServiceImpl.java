package ec.com.redepronik.negosys.invfac.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.invfac.dao.InteresVentaDao;
import ec.com.redepronik.negosys.invfac.entity.InteresVenta;

@Service
public class InteresVentaServiceImpl implements InteresVentaService {

	@Autowired
	private InteresVentaDao interesVentaDao;

	public void actualizar(InteresVenta interesVenta) {
		interesVentaDao.actualizar(interesVenta);
	}

	public void insertar(InteresVenta interesVenta) {
		interesVentaDao.insertar(interesVenta);
	}

	public List<InteresVenta> obtener() {
		return interesVentaDao.obtener(InteresVenta.class, "fechainicio", null);
	}

	public InteresVenta obtenerPorFecha(Date fecha) {
		// return interesVentaDao.obtenerPorId(Interesventa.class,
		// interesVentaId);
		return null;
	}

}
