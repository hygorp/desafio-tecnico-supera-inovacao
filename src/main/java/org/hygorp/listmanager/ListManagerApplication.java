package org.hygorp.listmanager;

import org.hygorp.listmanager.entities.ItemEntity;
import org.hygorp.listmanager.entities.TaskEntity;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;
import org.hygorp.listmanager.repositories.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@SpringBootApplication
@Configuration
public class ListManagerApplication implements CommandLineRunner {
    private final TaskRepository taskRepository;

    public ListManagerApplication(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ListManagerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        taskRepository.deleteAll();

        TaskEntity taskOne = new TaskEntity(
                "Projeto de Software",
                "Objetivo: Desenvolver um Gerenciador de Listas",
                LocalDate.now().plusDays(5)
        );

        taskOne.addItem(new ItemEntity(
                "Desenvolver Backend",
                "Utilizar algum framework Java",
                ItemPriorityEnum.Alta,
                ItemStateEnum.Completo
        ));
        taskOne.addItem(new ItemEntity(
                "Desenvolver Backend",
                "Utilizar algum framework adequado",
                ItemPriorityEnum.Media,
                ItemStateEnum.Completo
        ));
        taskOne.addItem(new ItemEntity(
                "Fazer Deploy da aplicação",
                "Utilizar algum seviço em nuvem adequado",
                ItemPriorityEnum.Baixa,
                ItemStateEnum.Pendente
        ));
        taskRepository.save(taskOne);

        TaskEntity taskTwo = new TaskEntity(
                "Construi casa do PET",
                "O objetivo é construir uma casa confortável para o cachorro",
                LocalDate.now().minusMonths(1)
        );
        taskTwo.addItem(new ItemEntity(
                "Comprar Madeiras",
                "Pesquisar melhores preços e madeiras resistentes",
                ItemPriorityEnum.Media,
                ItemStateEnum.Fazendo
        ));
        taskTwo.addItem(new ItemEntity(
                "Comprar Materiais para o Telhado",
                "Buscas melhores materiais para um telhado que não gere calor excessivo",
                ItemPriorityEnum.Media,
                ItemStateEnum.Fazendo
        ));
        taskTwo.addItem(new ItemEntity(
                "Construir a armação de madeira",
                "Começar a construção da base da casa",
                ItemPriorityEnum.Baixa,
                ItemStateEnum.Cancelado
        ));
        taskRepository.save(taskTwo);
    }
}
