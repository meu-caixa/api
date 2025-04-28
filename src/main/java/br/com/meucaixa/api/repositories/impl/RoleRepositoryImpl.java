package br.com.meucaixa.api.repositories.impl;

import br.com.meucaixa.api.annotations.WithSpan;
import br.com.meucaixa.api.exceptions.ValidationException;
import br.com.meucaixa.api.models.Role;
import br.com.meucaixa.api.repositories.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @WithSpan("Repository.findAllPageable")
    public Page<Role> findAllPageable(Pageable pageable) {
        try {
            int offset = pageable.getPageNumber() * pageable.getPageSize();
            int limit = pageable.getPageSize();

            String sortFormatted = pageable.getSort().toString().replace(":", "");
            String sort = pageable.getSort().isSorted() ? " ORDER BY " + sortFormatted : "";

            String query = "SELECT * FROM roles " + sort + " LIMIT ? OFFSET ?";

            var roles = jdbcTemplate.query(query, ps -> {
                ps.setInt(1, limit);
                ps.setInt(2, offset);
            }, new RoleRowMapper());

            int total = getTotalCount();
            return new PageImpl<>(roles, pageable, total);

        } catch (BadSqlGrammarException e) {
            throw new ValidationException("Invalid sort in query: " + pageable.getSort());
        }
    }

    @WithSpan("Repository.getTotalCount")
    private int getTotalCount() {
        String countQuery = "SELECT COUNT(*) FROM roles";
        return Objects.requireNonNullElse(jdbcTemplate.queryForObject(countQuery, Integer.class), 0);
    }

    private static class RoleRowMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

            return new Role(id, name, description, createdAt, updatedAt);
        }
    }
}
