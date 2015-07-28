package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Cotizacion;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class CotizacionDaoImpl extends GenericDaoImpl<Cotizacion, Integer>
		implements CotizacionDao {

}