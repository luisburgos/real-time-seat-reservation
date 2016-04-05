/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data;

import java.util.List;

/**
 *
 * @author luisburgos
 * @param <Entity>
 */
public abstract class Repository<Entity> {
    
    public abstract int save(Entity entity);
    
    public abstract int update(Entity entity);
    
    public abstract int delete(Entity entity);
    
    public abstract void deleteAll();
    
    public abstract List findAll();
    
    public abstract List findByName(int event_id);
    
}
