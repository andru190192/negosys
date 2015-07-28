package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Factura;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class FacturaDaoImpl extends GenericDaoImpl<Factura, Integer> implements
		FacturaDao {

}