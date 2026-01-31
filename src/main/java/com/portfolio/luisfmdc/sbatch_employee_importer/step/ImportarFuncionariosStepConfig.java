package com.portfolio.luisfmdc.sbatch_employee_importer.step;

import com.portfolio.luisfmdc.sbatch_employee_importer.domain.Funcionario;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class ImportarFuncionariosStepConfig {

    private final ItemStreamReader<Funcionario> synchronizedItemStreamReader;
    private final ItemProcessor<Funcionario, Funcionario> validatorItemProcessor;
    private final ItemWriter<Funcionario> funcionarioJdbcBatchItemWriter;
    private final AsyncTaskExecutor taskExecutor;

    @Bean
    public Step importarFuncionarioStep(JobRepository jobRepository) {
        return new StepBuilder("importarFuncionarioStep", jobRepository)
                .<Funcionario, Funcionario>chunk(250)
                .reader(synchronizedItemStreamReader)
                .processor(validatorItemProcessor)
                .writer(funcionarioJdbcBatchItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }
}