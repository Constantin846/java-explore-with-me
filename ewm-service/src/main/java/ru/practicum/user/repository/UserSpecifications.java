package ru.practicum.user.repository;
//todo
//public class UserSpecifications {
//    private UserSpecifications() {}
//
//    public static Specification<User> ad(List<Long> ids) {
//        return new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//
//            }
//        }
//
////        Root<Employee> root = q.from(Employee.class);
////        q.select(root);
////
////        List<String> parentList = Arrays.asList(new String[]{"John", "Raj"});
////
////        Expression<String> parentExpression = root.get(Employee_.Parent);
////        Predicate parentPredicate = parentExpression.in(parentList);
////        q.where(parentPredicate);
////        q.orderBy(cb.asc(root.get(Employee_.Parent));
////
////        q.getResultList();
//
//
//        /*CriteriaQuery<DeptEmployee> criteriaQuery =
//                criteriaBuilder.createQuery(DeptEmployee.class);
//        Root<DeptEmployee> root = criteriaQuery.from(DeptEmployee.class);
//        In<String> inClause = criteriaBuilder.in(root.get("title"));
//        for (String title : titles) {
//            inClause.value(title);
//        }
//        criteriaQuery.select(root).where(inClause);*/
//    }
//
//}
