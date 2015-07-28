package ec.com.redepronik.negosys.invfac.dao;

import java.math.BigInteger;

import ec.com.redepronik.negosys.invfac.entity.Producto;
import ec.com.redepronik.negosys.utils.dao.GenericDao;

public interface ProductoDao extends GenericDao<Producto, Long> {

	public BigInteger obtenerId();
}