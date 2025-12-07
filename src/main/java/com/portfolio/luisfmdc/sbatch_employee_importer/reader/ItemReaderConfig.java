package com.portfolio.luisfmdc.sbatch_employee_importer.reader;

import com.portfolio.luisfmdc.sbatch_employee_importer.domain.Funcionario;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ItemReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Funcionario> flatFileItemReader(
            @Value("#{jobParameters['resourceFile']}") Resource resource) {
        return new FlatFileItemReaderBuilder<Funcionario>()
                .name("flatFileItemReader")
                .resource(resource)
                .delimited()
                .names("nome", "email", "departamento", "salario", "dataAdmissao")
                .targetType(Funcionario.class)
                .build();
    }
}