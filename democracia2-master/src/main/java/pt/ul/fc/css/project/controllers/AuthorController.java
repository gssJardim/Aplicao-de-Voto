//package pt.ul.fc.css.project.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.lang.NonNull;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import pt.ul.fc.css.project.entities.Author;
//import pt.ul.fc.css.project.repositories.AuthorRepository;
//
//@RestController
//@RequestMapping("/api")
//public class AuthorController {
//	@Autowired
//	private AuthorRepository authorRepository;
//
//	public AuthorController(AuthorRepository authorRepository) {
//		this.authorRepository = authorRepository;
//	}
//
//	@PostMapping("/author")
//	public Long postAuthor(@NonNull @RequestBody Author author) {
//		Author saved = authorRepository.save(author);
//		return saved.getId();
//	}
//
//	@GetMapping("/author/{id}")
//	public Author getAuthor(@PathVariable Long id) {
//		return this.authorRepository.getReferenceById(id);
//	}
//
//	@GetMapping("/authors")
//	public List<Author> getAuthors(@RequestParam String query) {
//		return this.authorRepository.findByName(query);
//	}
//
//	@PutMapping("/author")
//	public Long putAuthor(@NonNull @RequestBody Author author) {
//		Author saved = authorRepository.save(author);
//		return saved.getId();
//	}
//
//	@RequestMapping(value = "/index")
//	public ModelAndView home() {
//		return new ModelAndView("index");
//	}
//}
