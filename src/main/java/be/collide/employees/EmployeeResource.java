package be.collide.employees;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/employees")
public class EmployeeResource {
    @Inject
    Employees employees;

    @GET
    public List<Employee> getAll() {
        return employees.findAll();
    }

    @GET
    @Path("{id}")
    public Employee getEmployee(@PathParam("id") String id) {
        return employees.get(id);
    }

    @POST
    public Response add(Employee employee, @Context UriInfo uriInfo) {
        employee.setId(UUID.randomUUID().toString());
        employees.add(employee);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(employee.getId());
        return Response.created(uriBuilder.build()).build();
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") String id, Employee employee) {
        Employee entity = employees.get(id);
        entity.setName(employee.getName());
        entity.setCompany(employee.getCompany());
        employees.add(employee);
    }

    @Transactional
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        employees.delete(id);
    }
}