package ec.com.redepronik.negosys.utils.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface GenericDao<T, K extends Serializable> {

	void actualizar(T t);

	Boolean comprobarIndices(Class<T> type, String atributo, String valor,
			String id);

	Boolean comprobarIndicesMinuscula(Class<T> type, String atributo,
			String valor, String id);

	Object contar(Class<T> type);

	Object contar(String consulta, Object[] valores);

	void eliminar(T t);

	void eliminarPorId(Class<T> type, K id);

	void evict(T t);

	void insertar(T t);

	List<T> obtener(Class<T> type, String atributoOrden, Boolean activo);

	List<T> obtenerListaPorAtributo(Class<T> type, String Atributo,
			String valorAtributo, Boolean activo);

	T obtenerPorAtributo(Class<T> type, String atributo, String valorAtributo,
			Boolean activo);

	List<T> obtenerPorHql(String consulta, Object[] valores);

	List<T> obtenerPorHql(String consulta, Object[] valores, int min, int max);

	T obtenerPorId(Class<T> type, K id);

	Session session();
}