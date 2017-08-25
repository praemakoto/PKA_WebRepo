package utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class HibernateUtil
{

    private static final Log LOG = LogFactory.getLog(HibernateUtil.class);
    private static final SessionFactory sessionFactory;

    static
    {
        try
        {
        	sessionFactory =   new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }
        catch (final Throwable ex)
        {
            LOG.error(ex);

            throw new ExceptionInInitializerError(ex);
        }
    }

    //~ Methods ································································

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws org.hibernate.HibernateException
     *          DOCUMENT ME!
     */
    public static Session currentSession()
            throws HibernateException
    {
        Session s = sessionFactory.getCurrentSession();
        Transaction tx = s.getTransaction();

        if(tx == null)
        {
            tx = s.beginTransaction();
        }

        if(!tx.isActive())
        {
            tx.begin();
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws org.hibernate.HibernateException
     *          DOCUMENT ME!
     */
    public static void closeSession()
            throws HibernateException
    {
        Session s = currentSession();

        if ((s != null) && s.isOpen())
        {
            s.flush();
            s.close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sql DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Query createQuery(final String sql)
    {
        return assignKnownParameters(currentSession().createQuery(sql));
    }

    /**
     * DOCUMENT ME!
     *
     * @param tx DOCUMENT ME!
     */
    public static void commit(final Transaction tx)
    {
        tx.getClass(); // check null

        if (tx.wasCommitted())
        {
            LOG.warn("Transaction was already committed [Committed="
                    + tx.wasCommitted() + ", RolledBack=" + tx.wasRolledBack()
                    + "].");
        }
        else
        {
            tx.commit();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tx DOCUMENT ME!
     */
    public static void rollback(final Transaction tx)
    {
        tx.getClass(); // check null

        if (tx.wasRolledBack())
        {
            LOG.warn("Transaction was already rolled back [Committed="
                    + tx.wasCommitted() + ", RolledBack=" + tx.wasRolledBack()
                    + "].");
        }
        else
        {
            tx.rollback();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param queryName DOCUMENT ME!
     * @param readOnly  DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Query getNamedQuery(final String queryName,
                                      final boolean readOnly)
    {
        return assignKnownParameters(currentSession().getNamedQuery(queryName)
                .setReadOnly(readOnly));
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz     DOCUMENT ME!
     * @param queryName DOCUMENT ME!
     * @param readOnly  DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Query getNamedQuery(final Class clazz,
                                      final String queryName,
                                      final boolean readOnly)
    {
        return getNamedQuery(clazz.getName() + "." + queryName, readOnly);
    }

    /**
     * DOCUMENT ME!
     *
     * @param queryName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Query getNamedQuery(final String queryName)
    {
        return getNamedQuery(queryName, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public static void save(final Object data)
            throws Exception
    {
        save(data, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param data  DOCUMENT ME!
     * @param evict DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public static void save(final Object data, final boolean flush)
            throws Exception
    {
        data.getClass(); // check null
        currentSession().clear();
        Session session = currentSession();
        Transaction tx = session.getTransaction();

        try
        {
            session.save(data);

            if (flush)
            {
                session.flush();
            }

            tx.commit();
        }
        catch (final Exception e)
        {
            tx.rollback();
            if(session!=null && session.isOpen())
            {
            	session.clear();
            }
            throw e;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public static void update(final Object data)
            throws Exception
    {
        update(data, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param data  DOCUMENT ME!
     * @param evict DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public static void update(final Object data, final boolean flush)
            throws Exception
    {
        data.getClass(); // check null
        
        Session session = currentSession();
        Transaction tx = session.getTransaction();

        try
        {
        	
//        	session.evict(data);
            session.update(data);

            if (flush)
            {
                session.flush();
            }

            tx.commit();
        }
        catch (final Exception e)
        {
            tx.rollback();
            if(session !=null && session.isOpen())
            {
            	session.clear();
            }

            throw e;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public static void saveOrUpdate(final Object data)
            throws Exception
    {
        saveOrUpdate(data, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param data  DOCUMENT ME!
     * @param evict DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public static void saveOrUpdate(final Object data, boolean flush)throws Exception
    {
        data.getClass(); // check null
        Session session = currentSession();
        Transaction tx = session.getTransaction();
        try
        {
            try
            {
                session.saveOrUpdate(data);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                session.merge(data);
            }

            if (flush)
            {
                session.flush();
            }

            tx.commit();
        }
        catch (final Exception e)
        {
            tx.rollback();
            
            if(session!=null && session.isOpen())
            {
            	session.clear();
            }

            throw e;
        }
    }
    
    public static void persist(final Object data, boolean flush)throws Exception
    {
        data.getClass(); // check null
        Session session = currentSession();
        Transaction tx = session.getTransaction();
        try
        {
            try
            {
                session.persist(data);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (flush)
            {
                session.flush();
            }

            tx.commit();
        }
        catch (final Exception e)
        {
            tx.rollback();
            
            if(session!=null && session.isOpen())
            {
            	session.clear();
            }

            throw e;
        }
    }
    
    public static void saveOrUpdateWithoutCommit(final Object data, boolean flush)  throws Exception
    {
    	 data.getClass(); // check null
         Session session = currentSession();

         Transaction tx = session.getTransaction();
         try
         {
             try
             {
                 session.saveOrUpdate(data);
             }
             catch (Exception e)
             {
                 e.printStackTrace();
                 session.merge(data);
             }

             if (flush)
             {
                 session.flush();
             }

         }
         catch (final Exception e)
         {
             tx.rollback();
             
             if(session!=null && session.isOpen())
             {
             	session.clear();
             }

             throw e;
         }
    }

    public static void delete(final Object data)
            throws Exception
    {
        delete(data, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param data  DOCUMENT ME!
     * @param evict DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public static void delete(final Object data, final boolean flush)
            throws Exception
    {
        data.getClass(); // check null
        Session session = currentSession();
        Transaction tx = session.getTransaction();

        try
        {
            session.delete(data);

            if (flush)
            {
                session.flush();
            }

            tx.commit();
        }
        catch (final Exception e)
        {
            tx.rollback();
            if(session != null & session.isOpen())
            {
            	session.clear();
            }

            throw e;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Query createQuery(final Class clazz)
    {
        return createQuery(clazz, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz    DOCUMENT ME!
     * @param readOnly DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Query createQuery(final Class clazz, final boolean readOnly)
    {
        return createQuery("FROM " + clazz.getName(), readOnly);
    }

    /**
     * DOCUMENT ME!
     *
     * @param queryString DOCUMENT ME!
     * @param readOnly    DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Query createQuery(final String queryString,
                                    final boolean readOnly)
    {
        return assignKnownParameters(currentSession().createQuery(queryString)
                .setReadOnly(readOnly));
    }

    /**
     * DOCUMENT ME!
     *
     * @param queryName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String getNamedQueryString(final String queryName)
    {
        return currentSession().getNamedQuery(queryName).getQueryString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz     DOCUMENT ME!
     * @param queryName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static String getNamedQueryString(final Class clazz,
                                             final String queryName)
    {
        return currentSession().getNamedQuery(clazz.getName() + "." + queryName)
                .getQueryString();
    }

    private static Query assignKnownParameters(final Query query)
    {
        String message = "Found known query parameter '%1$s', set it as %2$s.";

        for (final String param : query.getNamedParameters())
        {
            if ("now".equals(param))
            {
                Date now = new Date();
                query.setTimestamp(param, now);

                if (LOG.isDebugEnabled())
                {
                    LOG.debug(String.format(message, param, now.toString()));
                }
            }
            else if ("today".equals(param))
            {
                Date today = new Date();
                query.setDate(param, today);

                if (LOG.isDebugEnabled())
                {
                    LOG.debug(String.format(message, param, today.toString()));
                }
            }
        }

        return query;
    }


    public static void close(Connection conn, Statement stmt, ResultSet rs) throws SQLException
    {
        if(rs!=null)
        {
            rs.close();
        }
        if(stmt!=null)
        {
            stmt.close();
        }
        if(conn!=null && !conn.isClosed()){
            conn.close();
        }
    }
}
