package com.quasar.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.quasar.controllers.dto.AlbumWithPermissions;
import com.quasar.dao.PermissionsRepository;
import com.quasar.dao.RoleRepository;
import com.quasar.dao.UserRepository;
import com.quasar.dto.UserDTO;
import com.quasar.managers.AlbumManager;
import com.quasar.model.Album;
import com.quasar.model.AlbumPermission;
import com.quasar.security.ROLES;
import com.quasar.security.Role;
import com.quasar.security.User;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {

	private UserRepository userRepository;
	private AlbumManager albumManager;
	private PermissionsRepository permissionsRepository;
	private RoleRepository roleRepository;
	
	@Autowired
	public AdminController(UserRepository userRepository, AlbumManager albumManager, PermissionsRepository permissionsRepository,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.albumManager = albumManager;
		this.permissionsRepository = permissionsRepository;
		this.roleRepository = roleRepository;
	}

    @RequestMapping(
        value = {"/", ""},
        method = {RequestMethod.GET}
    )
    public ModelAndView getUserProfilePage(@RequestParam Optional<String> error) {
        return new ModelAndView("admin/administration");
    }
    
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView getUserListPage(@RequestParam(value = "withDeleted", required=false, defaultValue = "false") boolean withDeleted)
	{
		Map<String, Object> map = new HashMap<>();
		List<User> users = userRepository.findAll();
		Set<String> roles = new HashSet<String>();
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
		{
			roles.add(ga.getAuthority());
		}
		map.put("roles", roles);

		if (withDeleted)
			map.put("users", users);
		else
			map.put("users", users.stream().filter(u -> !u.isDeleted()).collect(Collectors.toList()));
		map.put("withDeleted", withDeleted);
		return new ModelAndView("admin/user_list", map);
	}
	
	
	@RequestMapping(value = "/show-roles", method = RequestMethod.GET)
	public ModelAndView getShowRolesPage()
	{
		return new ModelAndView("admin/show_roles");
	}
	
	@GetMapping("/{userId}/albumPermissions")
	public ModelAndView getAlbumPermissionsPage(@PathVariable String userId) {
		Map<String, Object> map = new HashMap<>();
		Set<Album> allAlbums = albumManager.getAllAlbums();
		Set<Album> userAlbums = albumManager.getAlbumsForUser(userId);
		
		Set<Album> noAccessAlbums = allAlbums.stream()
				.filter(a -> userAlbums.stream()
										.noneMatch(ua -> ua.getAlbumid().equals(a.getAlbumid()))).collect(Collectors.toSet());
		SortedSet<AlbumWithPermissions> albums = new TreeSet<>();
		albums.addAll(noAccessAlbums.stream().map(x -> new AlbumWithPermissions(x, false)).collect(Collectors.toSet()));
		albums.addAll(userAlbums.stream().map(x -> new AlbumWithPermissions(x, true)).collect(Collectors.toSet()));
		map.put("albums", albums);
		map.put("userId", userId);
		
		return new ModelAndView("admin/album_permissions", map);
	}
	
	@RequestMapping(path="/changeAlbumPermissions/{albumId}/{userId}/{isEnabled}", 
			method = RequestMethod.POST)
	public @ResponseBody Map<String, String> enableAlbumForUser(@PathVariable String albumId, @PathVariable String userId, @PathVariable boolean isEnabled, @RequestBody String body) {
		System.out.println("Changing permissions for user " + userId + ", album: " + albumId + " to " + (!isEnabled ? "ENABLED" : "DISABLED"));
		
		AlbumPermission albumPermission = permissionsRepository.findLastByAlbumAndUser(albumId, userId);
		
		Map<String, String> map = new HashMap<>();
		
		if (!isEnabled) { // we want to grant access to album
			if (albumPermission != null)
				map.put("result", "access already granted");
			else {
				permissionsRepository.save(new AlbumPermission.Builder()
						.setAlbumId(albumId)
						.setUserId(userId)
						.build());
				map.put("result", "access granted");
			}
		} else {  // we want to revoke access to album
			if (albumPermission != null) {
				permissionsRepository.save(new AlbumPermission.Builder()
						.setAlbumId(albumPermission.getId().getAlbumId())
						.setUserId(albumPermission.getId().getUserId())
						.setCreatedDate(albumPermission.getCreatedDate())
						.setDeletedDate(new Date())
						.build());
			
				map.put("result", "access revoked");
			} else {
				map.put("result", "access not granted");
			}			
		}
				
		return map;
	}
	
	@GetMapping(path = "/addUserForm")
	public ModelAndView addUserForm() {
		Map<String, Object> map = new HashMap<>();
		map.put("user", new UserDTO());
		List<String> roles = new ArrayList<>();
		for (ROLES role: ROLES.values()) {
			roles.add(role.name());
		}
		map.put("roles", roles);
		return new ModelAndView("admin/new_user", map);
	}
	
	@PostMapping(value="/user", consumes={"application/x-www-form-urlencoded"})
    public RedirectView submit(@Valid @ModelAttribute("user") UserDTO userDTO, @ModelAttribute("role") String role, BindingResult result) {
//        if (result.hasErrors()) {
//            return "error";
//        }
        
        Role roleFromDb = roleRepository.findByName(role);
        
        User user = new User(Collections.singleton(roleFromDb));
        user.setUsername(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        
        Map<String, Object> model = new HashMap<>();
        model.put("user", savedUser);
        return new RedirectView("/user-profile/");
    }	
	
	@DeleteMapping(value="/xxx/{userId}", consumes="application/json")
    public @ResponseBody void submit(@PathVariable String userId, @RequestBody String x) {
        
        User user = userRepository.findByUserid(userId);
        user.markAsDeleted();
        userRepository.save(user);

//        return new ResponseEntity<String>("DELETE Response", HttpStatus.OK);
    }
}
