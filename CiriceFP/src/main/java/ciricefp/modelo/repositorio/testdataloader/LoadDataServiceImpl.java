package ciricefp.modelo.repositorio.testdataloader;

import jakarta.persistence.EntityManager;

public class LoadDataServiceImpl implements LoadDataService {
    private final EntityManager em;
    private final LoadDataRepositorio repositorio;

    public LoadDataServiceImpl(EntityManager em) {
        this.em = em;
        this.repositorio = new LoadDataImpl(em);
    }

    @Override
    public int loadData() {
        try {
            em.getTransaction().begin();
            int result = repositorio.loadData();
            em.getTransaction().commit();
            return result;

        } catch (Exception e){
            System.out.println("Error al ejecutar la carga de datos de test.");
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean checkData() {
        try {
            return repositorio.checkData();
        } catch (Exception e){
            System.out.println("Error al ejecutar la carga de datos de test.");
            e.printStackTrace();
            return false;
        }
    }
}