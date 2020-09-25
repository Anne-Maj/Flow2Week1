package facades;

import dtos.PersonDTO;
import dtos.PersonsDTO;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public long getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            long personCount = (long) em.createQuery("SELECT COUNT(r) FROM Person r").getSingleResult();
            return personCount;
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new PersonNotFoundException(String.format("Person with id: (%d) not found", id));
        } else {
            try {
                em.getTransaction().begin();
                em.remove(person);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new PersonDTO(person);
        }
    }

        @Override
        public PersonDTO getPerson
        (int id) throws PersonNotFoundException {
            EntityManager em = getEntityManager();
            try {

                TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.id LIKE :id", Person.class);
                query.setParameter("id", id);
                Person person = query.getSingleResult();
                PersonDTO pDTO = new PersonDTO(person);
                return pDTO;
            } finally {
                em.close();
            }
        }

        @Override
        public PersonsDTO getAllPersons
        
            () {
        EntityManager em = getEntityManager();
            try {
                return new PersonsDTO(em.createNamedQuery("Person.getAllRows").getResultList());
            } finally {
                em.close();
            }
        }

        @Override
        public PersonDTO editPerson
        (PersonDTO p) throws PersonNotFoundException, MissingInputException {
            if ((p.getfName().length() == 0) || (p.getlName().length() == 0)) {
                throw new MissingInputException("First and last name required");

            }
            EntityManager em = getEntityManager();

            try {
                em.getTransaction().begin();

                Person person = em.find(Person.class, p.getId());
                if (person == null) {
                    throw new PersonNotFoundException(String.format("Person with id: (%d) not found", p.getId()));
                } else {
                    person.setFirstName(p.getfName());
                    person.setLastName(p.getlName());
                    person.setPhone(p.getPhone());

                }
                em.getTransaction().commit();
                return new PersonDTO(person);
            } finally {
                em.close();
            }

        }

        @Override
        public PersonDTO addPerson
        (String fName, String lName
        , String phone) throws MissingInputException {
            if ((fName.length() == 0) || (lName.length() == 0)) {
                throw new MissingInputException("First name and last name required");
            }
            EntityManager em = getEntityManager();
            Person person = new Person(fName, lName, phone);

            try {
                em.getTransaction().begin();

                em.persist(person);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new PersonDTO(person);
        }

    }
