package org.example.pahanaedu.service;

import org.example.pahanaedu.entity.Customer;
import org.example.pahanaedu.entity.Item;
import org.example.pahanaedu.entity.Order;
import org.example.pahanaedu.repository.CustomerRepository;
import org.example.pahanaedu.repository.ItemRepository;
import org.example.pahanaedu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDateTime startToday = today.atStartOfDay();
        LocalDateTime endToday = today.atTime(LocalTime.MAX);
        LocalDateTime startYesterday = yesterday.atStartOfDay();
        LocalDateTime endYesterday = yesterday.atTime(LocalTime.MAX);

        // Today's orders
        long todaysOrders = orderRepository.findAll().stream()
                .filter(o -> o.getOrderDate() != null && !o.getOrderDate().isBefore(startToday) && !o.getOrderDate().isAfter(endToday))
                .count();
        long yesterdaysOrders = orderRepository.findAll().stream()
                .filter(o -> o.getOrderDate() != null && !o.getOrderDate().isBefore(startYesterday) && !o.getOrderDate().isAfter(endYesterday))
                .count();
        double orderChange = yesterdaysOrders == 0 ? 100.0 : ((double) (todaysOrders - yesterdaysOrders) / yesterdaysOrders) * 100;
        stats.put("todaysOrders", todaysOrders);
        stats.put("orderChange", orderChange);

        // Total customers and new today
        long totalCustomers = customerRepository.count();
        long newCustomersToday = customerRepository.findAll().stream()
                .filter(c -> c.getId() != null && c instanceof Customer && c.getId() != null) // placeholder, ideally use createdAt
                .count(); // For demo, all customers are 'new'
        stats.put("totalCustomers", totalCustomers);
        stats.put("newCustomersToday", newCustomersToday);

        // Total items and low stock
        long totalItems = itemRepository.count();
        List<Item> lowStockItems = itemRepository.findAll().stream()
                .filter(i -> i.getStock() != null && i.getStock() <= 5)
                .collect(Collectors.toList());
        stats.put("totalItems", totalItems);
        stats.put("lowStockItems", lowStockItems);

        // Monthly revenue
        YearMonth thisMonth = YearMonth.now();
        YearMonth lastMonth = thisMonth.minusMonths(1);
        BigDecimal thisMonthRevenue = orderRepository.findAll().stream()
                .filter(o -> o.getOrderDate() != null && YearMonth.from(o.getOrderDate()).equals(thisMonth))
                .map(Order::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal lastMonthRevenue = orderRepository.findAll().stream()
                .filter(o -> o.getOrderDate() != null && YearMonth.from(o.getOrderDate()).equals(lastMonth))
                .map(Order::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        double revenueChange = lastMonthRevenue.compareTo(BigDecimal.ZERO) == 0 ? 100.0 : thisMonthRevenue.subtract(lastMonthRevenue).doubleValue() / lastMonthRevenue.doubleValue() * 100;
        stats.put("monthlyRevenue", thisMonthRevenue);
        stats.put("revenueChange", revenueChange);

        return stats;
    }

    public List<Map<String, Object>> getRecentActivity() {
        List<Map<String, Object>> activity = new ArrayList<>();
        // Recent orders
        orderRepository.findAll().stream()
                .sorted(Comparator.comparing(Order::getOrderDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .forEach(o -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "order");
                    map.put("orderId", o.getId());
                    map.put("customer", o.getCustomer() != null ? o.getCustomer().getName() : null);
                    map.put("time", o.getOrderDate());
                    activity.add(map);
                });
        // Recent customers
        customerRepository.findAll().stream()
                .sorted(Comparator.comparing(Customer::getId, Comparator.reverseOrder()))
                .limit(5)
                .forEach(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "customer");
                    map.put("customer", c.getName());
                    map.put("time", LocalDateTime.now()); // Placeholder
                    activity.add(map);
                });
        // Recent items
        itemRepository.findAll().stream()
                .sorted(Comparator.comparing(Item::getId, Comparator.reverseOrder()))
                .limit(5)
                .forEach(i -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "item");
                    map.put("item", i.getName());
                    map.put("time", LocalDateTime.now()); // Placeholder
                    activity.add(map);
                });
        // Low stock alerts
        itemRepository.findAll().stream()
                .filter(i -> i.getStock() != null && i.getStock() <= 5)
                .forEach(i -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", "lowStock");
                    map.put("item", i.getName());
                    map.put("time", LocalDateTime.now()); // Placeholder
                    activity.add(map);
                });
        // Sort by time descending (for demo, all times are now)
        activity.sort((a, b) -> ((LocalDateTime) b.get("time")).compareTo((LocalDateTime) a.get("time")));
        return activity;
    }
} 