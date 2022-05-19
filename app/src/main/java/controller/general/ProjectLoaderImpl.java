package controller.general;

import daw.manager.Manager;

public class ProjectLoaderImpl implements ProjectLoader {

    public Manager load() {
        return new Manager();
    }

}
