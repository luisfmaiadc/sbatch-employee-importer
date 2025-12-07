package com.portfolio.luisfmdc.sbatch_employee_importer.step;

import com.portfolio.luisfmdc.sbatch_employee_importer.domain.Funcionario;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ImportarFuncionariosStepConfig {

    private final ItemReader<Funcionario> flatFileItemReader;
    private final ItemProcessor<Funcionario, Funcionario> validatorItemProcessor;
    private final ItemWriter<Funcionario> funcionarioJdbcBatchItemWriter;

    @Bean
    public Step importarFuncionarioStep(JobRepository jobRepository) {
        return new StepBuilder("importarFuncionarioStep", jobRepository)
                .<Funcionario, Funcionario>chunk(500)
                .reader(flatFileItemReader)
                .processor(validatorItemProcessor)
                .writer(funcionarioJdbcBatchItemWriter)
                .build();
    }
}