package kitchenpos.order.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderLineItemRepository implements OrderLineItemRepository {

    private static final String TABLE_NAME = "order_line_item";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderLineItemRepository(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public OrderLineItem save(final OrderLineItem entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderLineItem> findAll() {
        final String sql = "SELECT id, order_id, ordered_menu_id, quantity FROM order_line_item";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        final String sql = "SELECT id, order_id, ordered_menu_id, quantity FROM order_line_item WHERE order_id = (:orderId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderId", orderId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private OrderLineItem select(final Long id) {
        final String sql = "SELECT id, order_id, ordered_menu_id, quantity FROM order_line_item WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private OrderLineItem toEntity(final ResultSet resultSet) throws SQLException {
        final Long id = resultSet.getLong(KEY_COLUMN_NAME);
        final Long orderId = resultSet.getLong("order_id");
        final Long menuId = resultSet.getLong("ordered_menu_id");
        final Long quantity = resultSet.getLong("quantity");
        return new OrderLineItem(id, orderId, menuId, quantity);
    }
}