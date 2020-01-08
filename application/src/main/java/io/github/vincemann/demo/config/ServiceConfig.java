package io.github.vincemann.demo.config;

import io.github.vincemann.demo.model.Owner;
import io.github.vincemann.demo.model.Pet;
import io.github.vincemann.demo.service.OwnerService;
import io.github.vincemann.demo.service.PetService;
import io.github.vincemann.demo.service.plugin.SaveNameToWordPressDb_Plugin;
import io.github.vincemann.demo.service.plugin.proxy.OwnerService_PluginProxy;
import io.github.vincemann.demo.service.plugin.proxy.PetService_PluginProxy;
import io.github.vincemann.generic.crud.lib.service.plugin.BiDirChildPlugin;
import io.github.vincemann.generic.crud.lib.service.plugin.BiDirParentPlugin;
import io.github.vincemann.generic.crud.lib.service.plugin.SessionReattachmentPlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceConfig {

    //always use ownerService with core plugins when injecting OwnerService
    @Primary
    @Bean
    public OwnerService extendedOwnerService(@Qualifier("basic") OwnerService ownerService,
                                             BiDirParentPlugin<Owner,Long> biDirParentPlugin,
                                             SaveNameToWordPressDb_Plugin saveNameToWordPressDbPlugin,
                                             SessionReattachmentPlugin sessionReattachmentPlugin){
        return new OwnerService_PluginProxy(ownerService,sessionReattachmentPlugin,biDirParentPlugin,saveNameToWordPressDbPlugin);
    }

    @Primary
    @Bean
    public PetService extendedPetService(@Qualifier("basic") PetService petService,
                                         BiDirChildPlugin<Pet,Long> biDirChildPlugin){
        return new PetService_PluginProxy(petService,biDirChildPlugin);
    }

}