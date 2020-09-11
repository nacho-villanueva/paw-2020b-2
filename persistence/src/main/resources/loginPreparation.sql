
insert into users(email,password)
    (select email,'password' from patients)
        on conflict (email) do nothing;

insert into users(email,password)
    (select email,'password' from medics)
on conflict (email) do nothing;

insert into users(email,password)
    (select email,'password' from clinics)
on conflict (email) do nothing;

update patients set (user_id) =
                        (select id from users
                         where users.email = patients.email);

update medics set (user_id) =
                      (select id from users
                       where users.email = medics.email);

update clinics set (user_id) =
                       (select id from users
                        where users.email = clinics.email);