package cz.cokrtvac.webgephi.ejb;

import cz.cokrtvac.webgephi.model.entity.GraphEntity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 4.6.13
 * Time: 19:36
 */
@Stateless
public class GraphService {
    @Inject
    private EntityManager em;

    public List<GraphEntity> getAll(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GraphEntity> criteria = cb.createQuery(GraphEntity.class);
        Root<GraphEntity> root = criteria.from(GraphEntity.class);

        return em.createQuery(criteria).getResultList();
    }

    public GraphEntity get(Long id){
        return em.find(GraphEntity.class, id);
    }

    public GraphEntity persist(GraphEntity e){
        em.persist(e);
        return e;
    }
}
