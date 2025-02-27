package nikolausus.sem5.kursachisbackend.controller;

import lombok.Getter;
import lombok.Setter;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.Mapper.UserMapper;
import nikolausus.sem5.kursachisbackend.entity.*;
import nikolausus.sem5.kursachisbackend.jwt.JwtUtil;
import nikolausus.sem5.kursachisbackend.repository.LogsOrdersRepository;
import nikolausus.sem5.kursachisbackend.service.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final ApplicationsService applicationsService;
    private final RoleService roleService;
    private final StatusApplicationsService statusApplicationsService;
    private final LogsApplicationsService logsApplicationsRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final CommentOnApplicationsService commentOnApplicationsService;
    private final OrdersService ordersService;
    private final CommentOnOrdersService commentOnOrdersService;
    private final LogsOrdersService logsOrdersService;
    private final StatusOrdersService statusOrdersService;
    private final LogsOrdersRepository logsOrdersRepository;

    public UserController(UserService userService, ApplicationsService applicationsService, JwtUtil jwtUtil,
                          UserDetailsService userDetailsService, RoleService roleService,
                          StatusApplicationsService statusApplicationsService,
                          LogsApplicationsService logsApplicationsRepository, CommentOnApplicationsService commentOnApplicationsService,
                          OrdersService ordersService, CommentOnOrdersService commentOnOrdersService, LogsOrdersService logsOrdersService,
                          StatusOrdersService statusOrdersService, LogsOrdersRepository logsOrdersRepository) {
        this.userService = userService;
        this.applicationsService = applicationsService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
        this.statusApplicationsService = statusApplicationsService;
        this.logsApplicationsRepository = logsApplicationsRepository;
        this.commentOnApplicationsService = commentOnApplicationsService;
        this.ordersService = ordersService;
        this.commentOnOrdersService = commentOnOrdersService;
        this.logsOrdersService = logsOrdersService;
        this.statusOrdersService = statusOrdersService;
        this.logsOrdersRepository = logsOrdersRepository;
    }

    @PostMapping("/application/send")
    public String sendApplication(@RequestBody Appl appl, @RequestHeader("Authorization") String jwt) {

        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();

        List<String> tokenRoles = jwtUtil.extractRoles(jwt);
        List<GrantedAuthority> authorities = tokenRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        boolean res = true;
        for (GrantedAuthority grantedAuthority : authorities) {
            if ((grantedAuthority.getAuthority().equals("ROLE_ADMIN") && appl.vibor == 3) ||
                    (grantedAuthority.getAuthority().equals("ROLE_MODER") && appl.vibor == 2) ||
                    (grantedAuthority.getAuthority().equals("ROLE_SPECIAL") && appl.vibor == 4) ||
                    appl.vibor < 2 || appl.vibor > 4) {
                res = false;
                break;
            }
        }

        if (!res) {
            return "Заявка на уже имеющуюся или не существующую роль";
        }

        if (applicationsService.checkApplicationDoesntExists(user_id, roleService.getRoleById(appl.vibor))) {
            return "У вас уже есть заявка на эту роль";
        }

        Applications applications = applicationsService.createApplication(
                userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")),
                roleService.getRoleById(appl.vibor), appl.pochemy);

        if (applications != null) {
            logsApplicationsRepository.createLog(user_id, applications.getId());
            return "Заявка успешно подана";
        } else {
            return "Какая-то ошибка";
        }
//        return res ? "Заявка успешно подана" : "Какая-то ошибка";
    }

    @PostMapping("/application/update")
    public String sendApplication(@RequestBody Appl2 appl2, @RequestHeader("Authorization") String jwt) {

        jwt = jwt.substring(7);

        return applicationsService.updateApplication(appl2.application_id, userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")), appl2.pochemy);
    }

    @GetMapping("/application/get_all_my_applications")
    public List<Applications> getAllMyApplications(@RequestHeader("Authorization") String jwt) {
//        System.out.println("Suchka");
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();

        return applicationsService.getAllApplicationsByUserId(user_id);
    }

    @GetMapping("/application/show_my_application")
    public Applications showMyApplication(@RequestHeader("Authorization") String jwt, @RequestParam Long application_id) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
        Applications applications = applicationsService.getApplicationsById(application_id).orElseThrow(() -> new RuntimeException("Не нашлась заявка"));
        if (!Objects.equals(applications.getUser().getId(), user_id)) {
            throw new RuntimeException("У вас нет прав на просмотр");
        }
        return applications;
    }

    @GetMapping("/application/get_comment")
    public CommentOnApplications showCommentOnApplication(@RequestHeader("Authorization") String jwt, @RequestParam Long application_id) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
        Applications applications = applicationsService.getApplicationsById(application_id).orElseThrow(() -> new RuntimeException("Не нашлась заявка"));
        if (!Objects.equals(applications.getUser().getId(), user_id)) {
            throw new RuntimeException("У вас нет прав на просмотр");
        }
        return commentOnApplicationsService.getCommentByApplicationId(application_id).orElseThrow();
    }

    @PostMapping("/orders/update")
    public String sendOrders(@RequestBody Ord ord, @RequestHeader("Authorization") String jwt, @RequestParam Long order_id) {
        try {
            Orders orders = ordersService.getOrderById(order_id).orElseThrow();
            jwt = jwt.substring(7);
            if (ordersService.existOnTargetIsu(ord.getTargetIsuNum()) && !Objects.equals(orders.getTarget_isu_num(), ord.getTargetIsuNum())) {
                throw new RuntimeException("Заказ на эту цель уже существует");
            }
            return ordersService.updateOrder(ord.order_id, userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")), ord.pochemy, ord.getTargetIsuNum(), ord.getTargetName());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/orders/get_all_my_orders")
    public List<Orders> getAllMyOrders(@RequestHeader("Authorization") String jwt) {
//        System.out.println("Suchka");
        jwt = jwt.substring(7);
        User user = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return ordersService.getAllOrdersByUserId(user);
    }

    @GetMapping("/orders/show_my_orders")
    public Orders showMyOrder(@RequestHeader("Authorization") String jwt, @RequestParam Long order_id) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
        Orders orders = ordersService.getOrderById(order_id).orElseThrow();
        if (!Objects.equals(orders.getUser().getId(), user_id)) {
            throw new RuntimeException("У вас нет прав на просмотр");
        }
        return orders;
    }

    @GetMapping("/orders/get_comment")
    public CommentOnOrders showCommentOnOrder(@RequestHeader("Authorization") String jwt, @RequestParam Long order_id) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
        Orders orders = ordersService.getOrderById(order_id).orElseThrow();
        if (!Objects.equals(orders.getUser().getId(), user_id)) {
            throw new RuntimeException("У вас нет прав на просмотр");
        }
        return commentOnOrdersService.getCommentByOrderId(order_id).orElseThrow();
    }


    @GetMapping("/hello")
    public String userHello() {
//        System.out.println("Запрос к юзеру");
        return "Hello, User!";
    }

    @GetMapping("/orders/all")
    public List<Orders> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @PostMapping("/orders/create")
    public String createOrder(@RequestHeader("Authorization") String jwt, @RequestBody CreateOrder createOrder) {
        try {
            if (ordersService.existOnTargetIsu(createOrder.getTargetIsuNum())) {
                throw new RuntimeException("Заказ на эту цель уже существует");
            }
            jwt = jwt.substring(7);
            User user = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            Orders orders = ordersService.createOrders(createOrder.getTargetName(), createOrder.getTargetIsuNum(), createOrder.getDescription(), user);
            logsOrdersRepository.createLog(user.getId(), orders.getId());
            return "Заказ отправлен на модерацию";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/test/userDTO")
    public UserDTO test_get_userDTO(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.substring(7);
        return UserMapper.toDTO(userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")));
    }
}


@Getter
@Setter
class CreateOrder {
    private String targetName;
    private Integer targetIsuNum;
    private String description;
}

@Getter
@Setter
class Appl {
    Long vibor;
    String pochemy;
}

@Getter
@Setter
class Appl2 {
    Long application_id;
    String pochemy;
}


@Getter
@Setter
class Ord {
    Long order_id;
    String pochemy;
    private String targetName;
    private Integer targetIsuNum;
}

