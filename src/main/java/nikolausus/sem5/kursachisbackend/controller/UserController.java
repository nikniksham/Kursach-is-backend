package nikolausus.sem5.kursachisbackend.controller;

import lombok.Getter;
import lombok.Setter;
import nikolausus.sem5.kursachisbackend.DTO.*;
import nikolausus.sem5.kursachisbackend.Mapper.UserMapper;
import nikolausus.sem5.kursachisbackend.jwt.JwtUtil;
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
    private final LogsApplicationsService logsApplicationsService;
    private final JwtUtil jwtUtil;
    private final CommentOnApplicationsService commentOnApplicationsService;
    private final OrdersService ordersService;
    private final CommentOnOrdersService commentOnOrdersService;
    private final LogsOrdersService logsOrdersService;

    public UserController(UserService userService, ApplicationsService applicationsService, JwtUtil jwtUtil,
                          RoleService roleService, LogsApplicationsService logsApplicationsService,
                          CommentOnApplicationsService commentOnApplicationsService, OrdersService ordersService,
                          CommentOnOrdersService commentOnOrdersService, LogsOrdersService logsOrdersService) {
        this.userService = userService;
        this.applicationsService = applicationsService;
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
        this.logsApplicationsService = logsApplicationsService;
        this.commentOnApplicationsService = commentOnApplicationsService;
        this.ordersService = ordersService;
        this.commentOnOrdersService = commentOnOrdersService;
        this.logsOrdersService = logsOrdersService;
    }

    @PostMapping("/application/send")
    public String sendApplication(@RequestBody Appl appl, @RequestHeader("Authorization") String jwt) {

        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();

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

        try {
            ApplicationsDTO applicationsDTO = applicationsService.createApplication(
                userService.getUserByLogin(jwtUtil.extractUsername(jwt)),
                roleService.getRoleById(appl.vibor), appl.pochemy);

            logsApplicationsService.createLog(user_id, applicationsDTO.getId());
            return "Заявка успешно подана";
        } catch (Exception e) {
            return "Error -> " + e.getMessage();
        }
//        return res ? "Заявка успешно подана" : "Какая-то ошибка";
    }

    @PostMapping("/application/update")
    public String sendApplication(@RequestBody Appl2 appl2, @RequestHeader("Authorization") String jwt) {

        jwt = jwt.substring(7);

        return applicationsService.updateApplication(appl2.application_id, userService.getUserByLogin(jwtUtil.extractUsername(jwt)), appl2.pochemy);
    }

    @GetMapping("/application/get_all_my_applications")
    public List<ApplicationsDTO> getAllMyApplications(@RequestHeader("Authorization") String jwt) {
//        System.out.println("Suchka");
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();

        return applicationsService.getAllApplicationsByUserId(user_id);
    }

    @GetMapping("/application/show_my_application")
    public ApplicationsDTO showMyApplication(@RequestHeader("Authorization") String jwt, @RequestParam Long application_id) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();
        ApplicationsDTO applicationsDTO = applicationsService.getApplicationsById(application_id);
        if (!Objects.equals(applicationsDTO.getUserDTO().getId(), user_id)) {
            throw new RuntimeException("У вас нет прав на просмотр");
        }
        return applicationsDTO;
    }

    @GetMapping("/application/get_comment")
    public CommentOnApplicationsDTO showCommentOnApplication(@RequestHeader("Authorization") String jwt, @RequestParam Long application_id) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();
        ApplicationsDTO applicationsDTO = applicationsService.getApplicationsById(application_id);
        if (!Objects.equals(applicationsDTO.getUserDTO().getId(), user_id)) {
            throw new RuntimeException("У вас нет прав на просмотр");
        }
        return commentOnApplicationsService.getCommentByApplication(applicationsDTO);
    }

    @PostMapping("/orders/update")
    public String sendOrders(@RequestBody Ord ord, @RequestHeader("Authorization") String jwt, @RequestParam Long order_id) {
        try {
            OrdersDTO ordersDTO = ordersService.getOrderById(order_id);
            jwt = jwt.substring(7);
            OrdersDTO exOrd = ordersService.getByTargetIsu(ord.getTargetIsuNum());
            if (exOrd != null && !Objects.equals(ordersDTO.getTarget_isu_num(), ord.getTargetIsuNum())) {
                throw new RuntimeException(exOrd.getId().toString());
            }
            return ordersService.updateOrder(ord.order_id, userService.getUserByLogin(jwtUtil.extractUsername(jwt)), ord.pochemy, ord.getTargetIsuNum(), ord.getTargetName());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/orders/get_all_my_orders")
    public List<OrdersDTO> getAllMyOrders(@RequestHeader("Authorization") String jwt) {
//        System.out.println("Suchka");
        jwt = jwt.substring(7);
        UserDTO userDTO = userService.getUserByLogin(jwtUtil.extractUsername(jwt));

        return ordersService.getAllOrdersByUserId(userDTO);
    }

    @GetMapping("/orders/show_my_orders")
    public OrdersDTO showMyOrder(@RequestHeader("Authorization") String jwt, @RequestParam Long order_id) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();
        OrdersDTO ordersDTO = ordersService.getOrderById(order_id);
        if (!Objects.equals(ordersDTO.getUserDTO().getId(), user_id)) {
            throw new RuntimeException("У вас нет прав на просмотр");
        }
        return ordersDTO;
    }

    @GetMapping("/orders/get_comment")
    public CommentOnOrdersDTO showCommentOnOrder(@RequestHeader("Authorization") String jwt, @RequestParam Long order_id) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();
        OrdersDTO ordersDTO = ordersService.getOrderById(order_id);
        if (!Objects.equals(ordersDTO.getUserDTO().getId(), user_id)) {
            throw new RuntimeException("У вас нет прав на просмотр");
        }
        return commentOnOrdersService.getCommentByOrderId(ordersDTO);
    }


    @GetMapping("/hello")
    public String userHello() {
//        System.out.println("Запрос к юзеру");
        return "Hello, User!";
    }

    @GetMapping("/orders/all")
    public List<OrdersDTO> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @PostMapping("/orders/create")
    public String createOrder(@RequestHeader("Authorization") String jwt, @RequestBody CreateOrder createOrder) {
        try {
            OrdersDTO exOrd = ordersService.getByTargetIsu(createOrder.getTargetIsuNum());
            if (exOrd != null) {
                throw new RuntimeException(exOrd.getId().toString());
            }
            jwt = jwt.substring(7);
            UserDTO userDTO = userService.getUserByLogin(jwtUtil.extractUsername(jwt));
            OrdersDTO ordersDTO = ordersService.createOrders(createOrder.getTargetName(), createOrder.getTargetIsuNum(), createOrder.getDescription(), userDTO);
            logsOrdersService.createLog(userDTO.getId(), ordersDTO.getId());
            return "Заказ отправлен на модерацию";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/test/userDTO")
    public UserDTO test_get_userDTO(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.substring(7);
        return userService.getUserByLogin(jwtUtil.extractUsername(jwt));
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

