package com.portfolio.luisfmdc.sbatch_employee_importer.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ImportarFuncionariosJobConfig {

    private final Step importarFuncionariosStep;

    @Bean
    public Job importarFuncionarioJob(JobRepository jobRepository) {
        return new JobBuilder("importarFuncionarioJob", jobRepository)
                .start(importarFuncionariosStep)
                .build();
    }
}