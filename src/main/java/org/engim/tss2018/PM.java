package org.engim.tss2018;

import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

/**
 * PM is shorthand of PersistenceManager. The class name is abbreviated so that
 * in the code you just type PM.db() and obtain the default EntityManager in
 * a very concise instruction.
 * @author lucio
 */
public class PM
{
  private static final PM singleton = new PM();

  private HashMap<String, EntityManagerFactory> emfs;
  
  private static final String mainUnitName = "salixweb-unit";
    
  public static interface EMapMBean extends Map<String, List<EMWrapper>>
  {
    
  }
  
  public static class EMap extends HashMap<String, List<EMWrapper>> implements EMapMBean
  {
    
  }
  
  public static interface EntityMapMXBean
  {
    EMap getMap();
    String[] getOverview();
    String[] getDetails();
  }

  private static EMap activeWrappers;
  
  public static class EntityMap implements EntityMapMXBean
  {
    @Override
    public EMap getMap()
    {
      return activeWrappers;
    }
    
    @Override
    public String[] getOverview()
    {
      List<String> result = new ArrayList<>();
      synchronized(activeWrappers)
      {
        Set<String> keys = activeWrappers.keySet();
        for (String s: keys)
          result.add(s + " -> " + activeWrappers.get(s).size());
      }
      String[] ares = new String[result.size()];
      return result.toArray(ares);
    }

    @Override
    public String[] getDetails()
    {
      List<String> lresult = new LinkedList<>();
      synchronized(activeWrappers)
      {
        Set<String> keys = activeWrappers.keySet();
        int ki = 0;
        for (String s: keys)
        {
          List<EMWrapper> wrappers = activeWrappers.get(s);
          String[][] traces = new String[wrappers.size()][];
          int i = 0;
          for (EMWrapper emw: wrappers)
            traces[i++] = emw.getStackStrace();
          lresult.add(flatten(traces));
        }
      }
      String[] result = new String[lresult.size()];
      return lresult.toArray(result);
    }

    private String flatten(String[][] traces)
    {
      StringBuilder sb = new StringBuilder();
      for (String[] key: traces)
        for (String values: key)
          sb.append(values).append("\n");
      return sb.toString();
    }

  }

  private PM()
  {
  }
  
  private static void initMBean()
  {
    synchronized(PM.class)
    {
      if (activeWrappers != null)
        return;
      
      try
      {
        activeWrappers = new EMap();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();  
        EntityMap map = new EntityMap();
        ObjectName name = new ObjectName(map.getClass().getPackage().getName() + ":type=" + map.getClass().getSimpleName());
        mbs.registerMBean(map, name);
      }
      catch (InstanceAlreadyExistsException | MBeanRegistrationException | MalformedObjectNameException | NotCompliantMBeanException ex)
      {
        Logger.getLogger(PM.class.getName()).log(Level.SEVERE, null, ex);
      }    
    }
  }

  public static PM getInstance()
  {
    return singleton;
  }

  private EMWrapper createEntityManager(String jndiName)
  {
    EntityManagerFactory emf = _getFactory(jndiName);
    EMWrapper result = new EMWrapper(emf.createEntityManager(), jndiName);
    return result;
  }
  
  private EntityManagerFactory _getFactory(String jndiName)
  {
    if (emfs == null)
      emfs = new HashMap<>();
    if (!emfs.containsKey(jndiName))
      emfs.put(jndiName, Persistence.createEntityManagerFactory(jndiName));
    return emfs.get(jndiName);
  }

  public void closeFactories()
  {
    if (emfs == null)
      return;
    for (EntityManagerFactory emf: emfs.values())
      emf.close();
    emfs.clear();
    emfs = null;
  }
  
  public static EMWrapper getEM()
  {
    return getInstance().createEntityManager(nameEM());
  }
  
  private static String suffix()
  {
    Thread current = Thread.currentThread();
    if (current.isDaemon())
      return "-bg";
    return "";
            
  }

  public static String nameEM()
  {
    return mainUnitName + suffix();
  }
  
  
  
  public static EMWrapper getEM(String unitname)
  {
    return getInstance().createEntityManager(unitname);
  }

  public static interface EMWrapperMBean extends EntityManager
  {
    
  };
  
  public static class EMWrapper implements EMWrapperMBean, AutoCloseable
  {
    private final EntityManager wrapped;
    private final String jndiName;
    private StackTraceElement[] ste;
    
    private EMWrapper(EntityManager wrapped, String jndiName)
    {
      this.wrapped = wrapped;
      this.jndiName = jndiName;
      this.ste = Thread.currentThread().getStackTrace();
      initMBean();
      synchronized(activeWrappers)
      {
        List<EMWrapper> active = activeWrappers.get(jndiName);
        if (active == null)
          active = new LinkedList<>();
        active.add(this);
        activeWrappers.put(jndiName, active);
      }
    }

    @Override
    public void persist(Object entity)
    {
      try
      {
        setUUID(entity, UUID.randomUUID().toString());
        wrapped.persist(entity);
      }
      finally
      {
        if (!wrapped.contains(entity))
          setUUID(entity, null);
      }
    }
    
    private void setUUID(Object entity, String uuid)
    {
      try
      {
        Method m = entity.getClass().getMethod("setUuid", String.class);
        m.invoke(entity, uuid);
      }
      catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException nsme)
      {
      }
    }
    
    @Override
    public <T> T merge(T entity)
    {
      return wrapped.<T>merge(entity);
    }

    @Override
    public void remove(Object entity)
    {
      wrapped.remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey)
    {
      return wrapped.<T>find(entityClass, primaryKey);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties)
    {
      return wrapped.<T>find(entityClass, primaryKey, properties);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode)
    {
      return wrapped.<T>find(entityClass, primaryKey, lockMode);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties)
    {
      return wrapped.<T>find(entityClass, primaryKey, lockMode, properties);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey)
    {
      return wrapped.<T>getReference(entityClass, primaryKey);
    }

    @Override
    public void flush()
    {
      wrapped.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode)
    {
      wrapped.setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode()
    {
      return wrapped.getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode)
    {
      wrapped.lock(entity, lockMode);
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties)
    {
      wrapped.lock(entity, lockMode, properties);
    }

    @Override
    public void refresh(Object entity)
    {
      wrapped.refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties)
    {
      wrapped.refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode)
    {
      wrapped.refresh(entity, lockMode);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties)
    {
      wrapped.refresh(entity, lockMode, properties);
    }

    @Override
    public void clear()
    {
      wrapped.clear();
    }

    @Override
    public void detach(Object entity)
    {
      wrapped.detach(entity);
    }

    @Override
    public boolean contains(Object entity)
    {
      return wrapped.contains(entity);
    }

    @Override
    public LockModeType getLockMode(Object entity)
    {
      return wrapped.getLockMode(entity);
    }

    @Override
    public void setProperty(String propertyName, Object value)
    {
      wrapped.setProperty(propertyName, value);
    }

    @Override
    public Map<String, Object> getProperties()
    {
      return wrapped.getProperties();
    }

    @Override
    public Query createQuery(String qlString)
    {
      return wrapped.createQuery(qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery)
    {
      return wrapped.<T>createQuery(criteriaQuery);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass)
    {
      return wrapped.<T>createNamedQuery(qlString, resultClass);
    }

    @Override
    public Query createNamedQuery(String name)
    {
      return wrapped.createNamedQuery(name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass)
    {
      return wrapped.<T>createNamedQuery(name, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString)
    {
      return wrapped.createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass)
    {
      return wrapped.createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping)
    {
      return wrapped.createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public void joinTransaction()
    {
      wrapped.joinTransaction();         
    }

    @Override
    public <T> T unwrap(Class<T> cls)
    {
      return wrapped.<T>unwrap(cls);
    }

    @Override
    public Object getDelegate()
    {
      return wrapped.getDelegate();
    }
    
    private EntityTransaction transaction;
    
    @Override
    public void close()
    {
      if (transaction != null)
        if (transaction.isActive())
          transaction.rollback();
       
      wrapped.close();
      this.ste = null;
      synchronized(activeWrappers)
      {
        List<EMWrapper> active = activeWrappers.get(jndiName);
        active.remove(this);
        activeWrappers.put(jndiName, active);
      }
    }

    @Override
    public boolean isOpen()
    {
      return wrapped.isOpen();
    }

    @Override
    public EntityTransaction getTransaction()
    {
      transaction = wrapped.getTransaction();
      return transaction;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory()
    {
      return wrapped.getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder()
    {
      return wrapped.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel()
    {
      return wrapped.getMetamodel();
    }
    
    public static HashMap<String, List<EMWrapper>> getWrappers()
    {
      return activeWrappers;
    }
    
    public String[] getStackStrace()
    {
      if (ste == null)
        return null;
      String[] result = new String[ste.length];
      int i = 0;
      for (StackTraceElement s: ste)
        result[i++] = s.getClassName() + "#" + s.getMethodName() + ":" + s.getLineNumber();
      return result;
    }

    @Override
    public Query createQuery(CriteriaUpdate cu)
    {
      return wrapped.createQuery(cu);
    }

    @Override
    public Query createQuery(CriteriaDelete cd)
    {
      return wrapped.createQuery(cd);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String string)
    {
      return wrapped.createNamedStoredProcedureQuery(string);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String string)
    {
      return wrapped.createStoredProcedureQuery(string);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String string, Class... types)
    {
      return wrapped.createStoredProcedureQuery(string, types);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String string, String... strings)
    {
      return wrapped.createStoredProcedureQuery(string, strings);
    }

    @Override
    public boolean isJoinedToTransaction()
    {
      return wrapped.isJoinedToTransaction();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> type)
    {
      return wrapped.createEntityGraph(type);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String string)
    {
      return wrapped.createEntityGraph(string);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String string)
    {
      return wrapped.getEntityGraph(string);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> type)
    {
      return wrapped.getEntityGraphs(type);
    }

  }

  public EntityManagerFactory getFactory(String jndiName)
  {
    if (emfs == null)
      emfs = new HashMap<>();
    if (!emfs.containsKey(jndiName))
      emfs.put(jndiName, Persistence.createEntityManagerFactory(jndiName));
    return emfs.get(jndiName);
  }
  
  public static EntityManager db()
  {
    // EntityManager em = 
    //   Persistence.createEntityManagerFactory(
    //      "engim-unit").createEntityManager();
    
    return getInstance().getFactory("engim-unit").createEntityManager();
  }
}
