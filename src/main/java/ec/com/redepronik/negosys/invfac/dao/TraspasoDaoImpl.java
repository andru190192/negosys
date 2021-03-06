package ec.com.redepronik.negosys.invfac.dao;

import org.springframework.stereotype.Repository;

import ec.com.redepronik.negosys.invfac.entity.Traspaso;
import ec.com.redepronik.negosys.utils.dao.GenericDaoImpl;

@Repository
public class TraspasoDaoImpl extends GenericDaoImpl<Traspaso, Integer>
		implements TraspasoDao {

}