package com.mmz.spring.beans.factory;



public interface ObjectFactory<T> {
	
	/**
	 * Return an instance (possibly shared or independent)
	 * of the object managed by this factory.
	 * @return an instance of the bean (should never be {@code null})
	 * @throws BeansException in case of creation errors
	 */
	T getObject();
}
