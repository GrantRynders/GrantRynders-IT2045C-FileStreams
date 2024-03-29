public class Product
{
    public String name;
    public String description;
    public String id;
    public double cost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
//        if (name.length() < 35)
//        {
//            for (int i = name.length(); i<=35; i++)
//            {
//                name = name + " ";
//            }
//            this.name = name;
//        }
//        else if (name.length() == 35)
//        {
//            this.name = name;
//        }
//        else if (name.length() > 35)
//        {
//            this.name = null;
//        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
//        if (description.length() < 75) {
//            for (int i = description.length(); i <= 75; i++) {
//                description = description + " ";
//            }
//            this.description = description;
//        }
//        else if (description.length() == 75)
//        {
//            this.description = description;
//        }
//        else if (description.length() > 75)
//        {
//            this.description = null;
//        }
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id)
    {
//        if (id.length() < 6)
//        {
//            for (int i = id.length(); i <= 6; i++) {
//                id = id + " ";
//            }
//            this.id = id;
//        }
//        else if (id.length() == 6)
//        {
//            this.id = id;
//        }
//        else if (id.length() > 6)
//        {
//            this.id = null;
//        }
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    public Product()
    {

    }
    public Product(String name, String description, String id, double cost)
    {
        this.setName(name);
        this.setDescription(description);
        this.setId(id);
        this.setCost(cost);
    }
}
