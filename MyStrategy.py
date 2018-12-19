import math


EPS = 1e-5


class Vec2:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def normalize(self):
        return self * (1 / self.len())

    def len(self):
        return math.sqrt(self.x * self.x + self.y * self.y)

    def __add__(a, b):
        return Vec2(a.x + b.x, a.y + b.y)

    def __sub__(a, b):
        return Vec2(a.x - b.x, a.y - b.y)

    def __mul__(a, b):
        return Vec2(a.x * b, a.y * b)


class MyStrategy:
    def act(self, me, rules, game, action):
        me.position = Vec2(me.x, me.z)
        me.velocity = Vec2(me.velocity_x, me.velocity_z)

        game.ball.position = Vec2(game.ball.x, game.ball.z)
        game.ball.velocity = Vec2(game.ball.velocity_x, game.ball.velocity_z)

        for i, robot in enumerate(game.robots):
            game.robots[i].position = Vec2(robot.x, robot.z)
            game.robots[i].velocity = Vec2(robot.velocity_x, robot.velocity_z)

        # Наша стратегия умеет играть только на земле
        # Поэтому, если мы не касаемся земли, будет использовать нитро
        # чтобы как можно быстрее попасть обратно на землю
        if not me.touch:
            action.target_velocity_x = 0
            action.target_velocity_y = -rules.MAX_ENTITY_SPEED
            action.target_velocity_z = 0
            action.jump_speed = 0
            # action.use_nitro = True

            return

        # Если при прыжке произойдет столкновение с мячом, и мы находимся
        # с той же стороны от мяча, что и наши ворота, прыгнем, тем самым
        # ударив по мячу сильнее в сторону противника
        jump = math.sqrt(
            (me.x - game.ball.x) ** 2 +
            (me.y - game.ball.y) ** 2 +
            (me.z - game.ball.z) ** 2
        ) < rules.BALL_RADIUS + rules.ROBOT_MAX_RADIUS and me.z < game.ball.z

        # Так как роботов несколько, определим нашу роль - защитник, или нападающий
        # Нападающим будем в том случае, если есть дружественный робот,
        # находящийся ближе к нашим воротам
        is_attacker = len(game.robots) == 2

        for robot in game.robots:
            if robot.is_teammate and robot.id != me.id:
                if robot.position.y < me.position.y:
                    is_attacker = True

        if is_attacker:
            # Стратегия нападающего:
            # Просимулирем примерное положение мяча в следующие 10 секунд, с точностью 0.1 секунда
            for i in range(1, 100 + 1):
                t = i * 0.1
                ball_pos = game.ball.position + game.ball.velocity * t
                # Если мяч не вылетит за пределы арены
                # (произойдет столкновение со стеной, которое мы не рассматриваем),
                # и при этом мяч будет находится ближе к вражеским воротам, чем робот,
                if ball_pos.y > me.position.y and abs(ball_pos.x) < (rules.arena.width / 2.0) and abs(ball_pos.y) < (rules.arena.depth / 2.0):
                    # Посчитаем, с какой скоростью робот должен бежать,
                    # Чтобы прийти туда же, где будет мяч, в то же самое время
                    delta_pos = Vec2(ball_pos.x, ball_pos.y) - Vec2(me.position.x, me.position.y)
                    need_speed = delta_pos.len() / t
                    # Если эта скорость лежит в допустимом отрезке
                    if 0.5 * rules.ROBOT_MAX_GROUND_SPEED < need_speed and need_speed < rules.ROBOT_MAX_GROUND_SPEED:
                        # То это и будет наше текущее действие
                        target_velocity = Vec2(delta_pos.x, delta_pos.y).normalize() * need_speed

                        action.target_velocity_x = target_velocity.x
                        action.target_velocity_y = 0
                        action.target_velocity_z = target_velocity.y
                        action.jump_speed = rules.ROBOT_MAX_JUMP_SPEED if jump else 0
                        # action.use_nitro = False

                        return

        # Стратегия защитника (или атакующего, не нашедшего хорошего момента для удара):
        # Будем стоять посередине наших ворот
        target_pos = Vec2(0, -(rules.arena.depth / 2) + rules.arena.bottom_radius)
        # Причем, если мяч движется в сторону наших ворот
        if game.ball.velocity.y < -EPS:
            # Найдем время и место, в котором мяч пересечет линию ворот
            t = (target_pos.y - game.ball.position.y) / game.ball.velocity.y
            x = game.ball.position.x + game.ball.velocity.x * t
            # Если это место - внутри ворот
            if abs(x) < (rules.arena.goal_width / 2):
                # То пойдем защищать его
                target_pos.x = x

        # Установка нужных полей для желаемого действия
        target_velocity = Vec2(
            target_pos.x - me.position.x,
            target_pos.y - me.position.y,
        ) * rules.ROBOT_MAX_GROUND_SPEED

        action.target_velocity_x = target_velocity.x
        action.target_velocity_y = 0
        action.target_velocity_z = target_velocity.y
        action.jump_speed = rules.ROBOT_MAX_JUMP_SPEED if jump else 0
        # action.use_nitro = False
