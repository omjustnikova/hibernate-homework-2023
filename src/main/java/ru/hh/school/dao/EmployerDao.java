package ru.hh.school.dao;

import java.util.Collections;
import java.util.List;
import org.hibernate.SessionFactory;
import ru.hh.school.entity.Employer;
import ru.hh.school.entity.Vacancy;

public class EmployerDao extends GenericDao {

  public EmployerDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * TODO: здесь нужен метод, позволяющий сразу загрузить вакасии, связанные с работодателем и в некоторых случаях
   * избежать org.hibernate.LazyInitializationException
   * Также в запрос должен передаваться параметр employerId
   * <p>
   * https://vladmihalcea.com/the-best-way-to-handle-the-lazyinitializationexception/
   */
  public Employer getEager(int employerId) {
    return getSession()
        .createQuery("from Employer employer " +
                        "left join fetch employer.vacancies " +
                        "where employer.id = :employerId", Employer.class)
        .setParameter("employerId", employerId)
        .getSingleResult();
  }

}
