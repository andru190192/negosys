package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entityAux.VolumenVentaFactura;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class VolumenVentaFacturaDaoImpl extends
		GenericDaoImpl<VolumenVentaFactura, Integer> implements
		VolumenVentaFacturaDao {

}