package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Pedido;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class PedidoDaoImpl extends GenericDaoImpl<Pedido, Integer> implements
		PedidoDao {

}