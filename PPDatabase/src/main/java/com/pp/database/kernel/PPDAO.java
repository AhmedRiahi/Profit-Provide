package com.pp.database.kernel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;


public class PPDAO<T extends PPEntity> extends BasicDAO<T,ObjectId>{

	public PPDAO(Class<T> entityClass){
		super(entityClass,MongoDatastore.getDatastore());
	}
	
	public void setDatastore(Datastore datastore) {
		this.ds = (DatastoreImpl) datastore;
		this.initType(this.getEntityClass());
	}
	
	@Override
	public Key<T> save(T entity) {
		PPEntity ppEntity = (PPEntity) entity;
		if( ppEntity.getCreationDate() == null){
			ppEntity.setCreationDate(new Date());
		}else{
			ppEntity.setLastModificationDate(new Date());
		}
		
		return super.save((T) ppEntity);
	}
	
	public void saveAll(List<T> entities){
		for(T entity : entities){
			this.save(entity);
		}
	}
	
	public T get(String hexId){
		ObjectId oid = new ObjectId(hexId);
		return this.get(oid);
	}
	
	public T get(ObjectId id){
		return this.createQuery().field("id").equal(id).get();
	}

	public void delete(String hexId){
		ObjectId oid = new ObjectId(hexId);
		this.deleteById(oid);
	}

	public List<T> getFrom(int from,int stepSize){
		return this.createQuery().offset(from).limit(stepSize).asList();
	}
	
	public void updateCollection(T entity,String field,Collection<?> updateValue){
		Query<T> query = this.createQuery().field("_id").equal(entity.getId());
		UpdateOperations<T> updateOperation = this.createUpdateOperations().set(field, updateValue);
		this.update(query,updateOperation);
	}
	
}