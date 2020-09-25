/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tirsdag.jpademo.entities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import static tirsdag.jpademo.entities.Person_.fees;

/**
 *
 * @author 45222
 */
public class Tester {
    
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        
        Person p1 = new Person("Jønke", 1963);
        Person p2 = new Person("Blondie", 1959);
        
        Address a1 = new Address("Store Torv 1", 2323, "Nr. Snede");
        Address a2 = new Address("Langgade 34", 1212, "Valby");
        
        p1.setAddress(a1);
        p2.setAddress(a2);
        
        Fee f1 = new Fee(100);
        Fee f2 = new Fee(200);
        Fee f3 = new Fee(300);
        
        SwimStyle s1 = new SwimStyle("Crawl");
        SwimStyle s2 = new SwimStyle ("Breast stroke");
        SwimStyle s3 = new SwimStyle ("Butterfly");
        
        p1.addSwimStyle(s1);
        p1.addSwimStyle(s2);
        p2.addSwimStyle(s3);
        
        p1.addFee(f1);
        p1.addFee(f2);
        p2.addFee(f3);
        
        em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            //em.persist(a1); - behøves ikke længere, da der bruges cascadetype.persist i Person.java
            //em.persist(a2);
            
        em.getTransaction().commit();
        
          em.getTransaction().begin();
            p1.removeSwimStyle(s1);
            
        em.getTransaction().commit();
        
        System.out.println("P1: "+p1.getP_id() + ", "+p1.getName());
        System.out.println("P2: "+p2.getP_id()+", "+p2.getName());
        
        System.out.println("Jønskes gade: "+p1.getAddress().getStreet());
        
        System.out.println("Lad os se om tovejs virker: "+a1.getPerson().getName());
        
        System.out.println("Hvem har betalt f2? " +f2.getPerson().getName());
        
        System.out.println("Hvad er der blevet betalt i alt?");
        TypedQuery<Fee> q1 = em.createQuery("SELECT f FROM Fee f", Fee.class);
        List<Fee> fees = q1.getResultList();
        
        for (Fee f: fees ){
            System.out.println(f.getPerson().getName() + ": "+f.getAmount()+" kr., den "+ f.getPayDate()+ " Adr: "+ f.getPerson().getAddress().getCity());
        }
        
        
    }
    
}
