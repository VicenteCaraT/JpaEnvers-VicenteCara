package org.example;



import org.example.entidades.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");
        EntityManager entityManager = emf.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Categoria perecedero = Categoria.builder()
                    .denominacion("perecederos")
                    .build();
            Categoria lacteos = Categoria.builder()
                    .denominacion("lacteos")
                    .build();
            Categoria limpieza = Categoria.builder()
                    .denominacion("limpieza")
                    .build();

            Articulo leche = Articulo.builder()
                    .cantidad(10)
                    .denominacion("leche1")
                    .precio(1000)
                    .build();

            Articulo detergente = Articulo.builder()
                    .cantidad(12)
                    .denominacion("detergente")
                    .precio(500)
                    .build();

            leche.getCategorias().add(perecedero);
            leche.getCategorias().add(lacteos);
            detergente.getCategorias().add(limpieza);

            lacteos.getArticulos().add(leche);
            perecedero.getArticulos().add(leche);
            limpieza.getArticulos().add(detergente);

            Factura fac1 = Factura.builder()
                    .fecha("27/08/2024")
                    .numero(40)
                    .total(10000)
                    .build();

            Cliente cli1 = Cliente.builder()
                    .nombre("Vicente")
                    .apellido("Cara")
                    .dni(44909938)
                    .build();
            Domicilio domicilio = Domicilio.builder()
                    .nombreCalle("Suipacha")
                    .numero(241)
                    .build();

            Cliente cli2 = Cliente.builder()
                    .nombre("Tadeo")
                    .apellido("Drube")
                    .dni(44122610)
                    .build();

            Domicilio domicilio2 = Domicilio.builder()
                    .nombreCalle("Suipacha")
                    .numero(240)
                    .build();


            cli1.setDomicilio(domicilio);
            cli2.setDomicilio(domicilio2);

            fac1.setCliente(cli1);

            DetalleFactura detalleFactura = DetalleFactura.builder()
                    .cantidad(10)
                    .subTotal(10000)
                    .build();


            detalleFactura.setArticulo(leche);

            fac1.getDetalles().add(detalleFactura);

            DetalleFactura detalleFactura2 = DetalleFactura.builder()
                    .cantidad(12)
                    .subTotal(6000)
                    .build();

            detalleFactura2.setArticulo(detergente);

            fac1.getDetalles().add(detalleFactura2);

            entityManager.persist(fac1);
            entityManager.flush();
            entityManager.getTransaction().commit();

            // Actualizar la cliente
            /*entityManager.getTransaction().begin();
            cli1.setDni(44912312);
            cli1.setApellido("Tapia");
            entityManager.merge(cli1);
            entityManager.getTransaction().commit();*/

            // Buscar la cliente por ID
            Cliente clienteEncontrado = entityManager.find(Cliente.class, cli1.getId());

            System.out.println("Cliente encontrado: " + clienteEncontrado);


            // Desconectar la entidad (estado Detached)
            entityManager.getTransaction().begin();
            entityManager.detach(cli2);
            entityManager.getTransaction().commit();

            System.out.println("Voy a eliminar cliente que ya no está vinculada");
            // Eliminar el cliente
            /*entityManager.getTransaction().begin();
            entityManager.remove(cli2);
            entityManager.getTransaction().commit();*/


            System.out.println("Me tiene que dar error");
            //Buscar la persona por ID
            //Cliente personaEncontrada2 = entityManager.find(Cliente.class, cli2.getId());

            //System.out.println("Persona encontrada desde la base de datos: " + personaEncontrada2);


        }catch (Exception e){
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
            System.out.println("Error...");
        }

    }
}