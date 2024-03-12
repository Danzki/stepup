package com.danzki.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Metamodel;
import org.hibernate.metamodel.MappingMetamodel;
import org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;

public interface ValidationService {
   default String getSchemaAndTableByClass(EntityManager entityManager, Class clazz) {
      Metamodel metamodel = entityManager.getMetamodel();
      MappingMetamodel mappingMetamodel = ((MappingMetamodelImpl)metamodel).getMappingMetamodel();
      EntityPersister entityPersister = ((MappingMetamodelImpl)mappingMetamodel).entityPersister(clazz.getName());
      SingleTableEntityPersister entityPersisterImpl = (SingleTableEntityPersister)entityPersister.getEntityPersister();
      return "public." + entityPersisterImpl.getTableName();
   }
}
