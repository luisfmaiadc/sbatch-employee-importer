package com.portfolio.luisfmdc.sbatch_employee_importer.writer;

import com.portfolio.luisfmdc.sbatch_employee_importer.domain.Funcionario;
import org.springframework.batch.infrastructure.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ItemWriterConfig {

    private static final String INSERT_FUNCIONARIO_SQL =
            "INSERT INTO TbFuncionario (nome, email, departamento, salario, dataAdmissao, dataCriacao) " +
                    "VALUES (?, ?, ?, ?, ?, NOW())";

    @Bean
    public JdbcBatchItemWriter<Funcionario> funcionarioJdbcBatchItemWriter(
            @Qualifier("appDataSource") DataSource appDataSource) {
        return new JdbcBatchItemWriterBuilder<Funcionario>()
                .dataSource(appDataSource)
                .sql(INSERT_FUNCIONARIO_SQL)
                .itemPreparedStatementSetter(itemPreparedStatementSetter())
                .build();
    }

    private ItemPreparedStatementSetter<Funcionario> itemPreparedStatementSetter() {
        return (funcionario, ps) -> {
            ps.setString(1, funcionario.getNome());
            ps.setString(2, funcionario.getEmail());
            ps.setString(3, funcionario.getDepartamento());
            ps.setDouble(4, Double.parseDouble(funcionario.getSalario()));
            ps.setString(5, funcionario.getDataAdmissao());
        };
    }
}