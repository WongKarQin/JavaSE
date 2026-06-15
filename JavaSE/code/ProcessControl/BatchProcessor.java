package ProcessControl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据遍历与批量处理
 */
public class BatchProcessor {

    // ==================== 批量导入用户 ====================

    /**
     * 批量导入用户：使用循环 + 异常分支
     */
    public BatchResult importUsers(List<UserImportDTO> users) {
        List<String> errors = new ArrayList<>();
        List<User> success = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            UserImportDTO dto = users.get(i);
            try {
                // 数据校验
                validateUser(dto);
                // 转换并保存
                User user = UserMapper.toEntity(dto);
                userRepository.save(user);
                success.add(user);
            } catch (ValidationException e) {
                // 记录错误，继续处理下一条
                errors.add(String.format("第%d行：%s", i + 1, e.getMessage()));
            } catch (DuplicateKeyException e) {
                errors.add(String.format("第%d行: 用户已存在", i + 1));
            } catch (Exception e) {
                // 未知错误，记录并终止
                errors.add(String.format("第%d行: 系统错误", i + 1));
                break;  // 终止批量处理
            }
        }
        return new BatchResult(success.size(), errors.size(), errors);
    }

    // ==================== 数据筛选 ====================

    /**
     * 使用 Stream 进行数据筛选（更现代的方式）
     */
    public List<User> filterActiveUsers(List<User> users) {
        return users.stream()
                .filter(User::isActive)
                .filter(u -> u.getLastLoginTime().isAfter(LocalDateTime.now().minusDays(30)))
                .sorted(Comparator.comparing(User::getLastLoginTime).reversed())
                .limit(100)
                .collect(Collectors.toList());
    }

    // ==================== 辅助方法 ====================

    private void validateUser(UserImportDTO dto) {
        if (dto == null) {
            throw new ValidationException("用户数据为空");
        }
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new ValidationException("用户名不能为空");
        }
        if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
            throw new ValidationException("邮箱格式不正确");
        }
    }

    // ==================== 模拟依赖（实际项目中应注入） ====================

    private final UserRepository userRepository = new UserRepository();
    private final UserMapper userMapper = new UserMapper();

    // ==================== 内部类定义 ====================

    /**
     * 批量处理结果
     */
    public static class BatchResult {
        private int successCount;
        private int errorCount;
        private List<String> errorMessages;

        public BatchResult(int successCount, int errorCount, List<String> errorMessages) {
            this.successCount = successCount;
            this.errorCount = errorCount;
            this.errorMessages = errorMessages;
        }

        @Override
        public String toString() {
            return String.format("BatchResult{成功=%d, 失败=%d, 错误=%s}",
                    successCount, errorCount, errorMessages);
        }
    }

    /**
     * 用户导入DTO
     */
    public static class UserImportDTO {
        private String username;
        private String email;
        private String phone;

        public UserImportDTO(String username, String email, String phone) {
            this.username = username;
            this.email = email;
            this.phone = phone;
        }

        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }

    /**
     * 用户实体
     */
    public static class User {
        private Long id;
        private String username;
        private boolean active;
        private LocalDateTime lastLoginTime;

        public User(Long id, String username, boolean active, LocalDateTime lastLoginTime) {
            this.id = id;
            this.username = username;
            this.active = active;
            this.lastLoginTime = lastLoginTime;
        }

        public boolean isActive() { return active; }
        public LocalDateTime getLastLoginTime() { return lastLoginTime; }
        public String getUsername() { return username; }
        public Long getId() { return id; }
    }

    /**
     * 自定义校验异常
     */
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    /**
     * 重复键异常
     */
    public static class DuplicateKeyException extends RuntimeException {
        public DuplicateKeyException(String message) {
            super(message);
        }
    }

    /**
     * 用户转换器
     */
    public static class UserMapper {
        public static User toEntity(UserImportDTO dto) {
            return new User(
                    System.currentTimeMillis(),  // 模拟生成ID
                    dto.getUsername(),
                    true,
                    LocalDateTime.now()
            );
        }
    }

    /**
     * 用户仓库（模拟）
     */
    public static class UserRepository {
        private final List<User> database = new ArrayList<>();

        public void save(User user) {
            // 模拟重复检测
            boolean exists = database.stream()
                    .anyMatch(u -> u.getUsername().equals(user.getUsername()));
            if (exists) {
                throw new DuplicateKeyException("用户已存在: " + user.getUsername());
            }
            database.add(user);
        }

        public List<User> findAll() {
            return new ArrayList<>(database);
        }
    }

    // ==================== 测试入口 ====================

    public static void main(String[] args) {
        BatchProcessor processor = new BatchProcessor();

        // 准备测试数据
        List<UserImportDTO> importList = new ArrayList<>();
        importList.add(new UserImportDTO("张三", "zhangsan@example.com", "13800138000"));  // 正常
        importList.add(new UserImportDTO("", "invalid", "13800138001"));                   // 用户名空
        importList.add(new UserImportDTO("李四", "lisi@example.com", "13800138002"));       // 正常
        importList.add(new UserImportDTO("王五", "wangwu@example.com", "13800138003"));     // 正常

        System.out.println("=== 测试1：批量导入 ===");
        BatchResult result = processor.importUsers(importList);
        System.out.println(result);

        // 第二次导入，触发重复
        System.out.println("\n=== 测试2：重复导入 ===");
        BatchResult result2 = processor.importUsers(importList);
        System.out.println(result2);

        // 测试 Stream 筛选
        System.out.println("\n=== 测试3：Stream 筛选活跃用户 ===");
        List<User> allUsers = processor.userRepository.findAll();
        // 模拟一些不活跃用户
        List<User> testUsers = new ArrayList<>();
        testUsers.add(new User(1L, "活跃用户A", true, LocalDateTime.now().minusDays(5)));
        testUsers.add(new User(2L, "活跃用户B", true, LocalDateTime.now().minusDays(10)));
        testUsers.add(new User(3L, "不活跃用户", true, LocalDateTime.now().minusDays(60)));
        testUsers.add(new User(4L, "禁用用户", false, LocalDateTime.now().minusDays(3)));

        List<User> activeUsers = processor.filterActiveUsers(testUsers);
        System.out.println("筛选结果数量: " + activeUsers.size());
        activeUsers.forEach(u -> System.out.println("  - " + u.getUsername()));
    }
}