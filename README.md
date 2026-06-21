# GUI-Based Inventory Management System

An automated, object-oriented Java desktop application designed to streamline stock tracking, price management, and perishable product oversight for small businesses. The system replaces manual logging errors with strong database-like text file permanence, custom multi-tier exception shielding, and a graphical dashboard interface.

---

## System Architecture and Class Relationships

The application is structured following clean separation of concerns across data entities, business rules, custom exceptions, and presentation spaces:

* **Model Layer:** Uses an inheritance hierarchy where base data patterns are established by a generic product type, which is extended by a perishable specialized module tracking expiration timelines dynamically.
* **Service/Management Layer:** Functions as the operational heart. It maintains in-memory array caches, enforces operational workflows, handles data mapping parameters, and executes byte-level synchronization to disk.
* **View/GUI Layer:** Built entirely on Swing visual containers. Converts raw operations into explicit layout interactions, using interactive tables, dialogue boxes, and error notifications to prevent manual terminal entry mistakes.

### Structural Topology and Data Paths

```text
  [ User Interface: InventoryGUI ] 
                 |
                 v (Triggers CRUD Actions)
  [ Operational Controller: InventoryManager ] <---> [ Custom Exception Vectors ]
                 |
        +--------+--------+
        |                 |
        v                 v
  [ Entity Cache ]   [ I/O Pipeline ] ---> Data File: data/inventory_data.txt
```
## Software Component Matrix and Design

| Module / File | Operational Responsibility | Core Object-Oriented Interface / API |
| :--- | :--- | :--- |
| `model/Product.java` | Defines basic structure for non-expiring goods, providing field encapsulation and string serialization maps. | Encapsulation, Method Overriding |
| `model/PerishableProduct.java` | Extends basic structure to support time-sensitive inventory using tracking metrics. | Inheritance, Polymorphism, `LocalDate` |
| `service/InventoryManager.java` | Manages stock array tracking, entry searches, sorting metrics, and handles database text file reads/writes. | Multi-Catch Blocks, File I/O Streams |
| `view/InventoryGUI.java` | Visual controller rendering grid layouts, data modification forms, and routing input events safely. | `javax.swing.*`, `java.awt.*` |
| `exception/*` | Set of custom error contexts protecting structural boundaries from duplicate keys, empty fields, or full bounds. | `java.lang.Exception` subclassing |

### Architectural Robustness Features
* **Encapsulation and Type Validation:** Fields use tight visibility restrictions. Access is routed through strict getters and setters that filter out negative values and missing name inputs.
* **State Preservation Resilience:** System workflows call atomic file sync routines instantly upon data updates to avoid volatile memory losses if runtime crashes occur.
* **Custom Exceptional Shielding:** Uses explicit custom exception definitions rather than generic runtime blocks, allowing the GUI to intercept exact point failures and present precise user feedback messages.

### System Validation and Operational Verification
Test Case 1 (Duplicate Key Rejection): Attempting to register an inventory code that matches an existing record blocks data submission and triggers a warning dialog. [Pass]

Test Case 2 (Polymorphic Sorting and Expiry Management): Mixing standard items with expiring variations confirms that deadline properties evaluate correctly across polymorphic view matrices. [Pass]

Test Case 3 (File Storage Parity): Modifying values inside the visual dashboard and inspecting the text file values proves that accurate state updates write to persistent storage. [Pass]

Test Case 4 (Input Error Interception and Graceful Recovery):

Issue: Inputting alphabetic text inside numeric pricing fields triggered unhandled framework crash flags during initial execution sweeps.

Resolution: Structured key processing blocks inside numeric parsing checks inside the GUI layer, matching standard validation routines to ensure smooth app behavior. [Pass]

### Engineering Team and Roles
Ibrahim Alamgir: Software Engineering Lead (Core Application Architecture, Object Hierarchy Design, Custom Exception Infrastructure, Input Validation Engine).

Adnan Athar: User Interface and Asset Lead (Swing Container Assembly, Event Dispatch Handling, File I/O Management, Functional Verification Matrix).

### Project Context and References
Developed as a lab project under the advanced guidelines of Object-Oriented Development frameworks.

Arnold, K., Gosling, J., & Holmes, D. (2015). The Java Programming Language: Core Architecture and Design Principles.

Oracle Documentation. (2024). Java Foundation Classes: Creating Graphical User Interfaces with Swing.
