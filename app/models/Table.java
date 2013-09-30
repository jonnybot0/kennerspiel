package models;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.annotation.*;
import game.Board;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@JsonSerialize(using = Table.Serializer.class)
public class Table extends Model {
	
	static class Serializer extends JsonSerializer<Table>{

		/**
		 * Overrides normal serialize operation by adding the table.id field
		 */
		@Override
		public void serialize(Table table, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			
			jgen.writeStartObject();
			jgen.writeNumberField("id", table.getId());
			jgen.writeStringField("name", table.getName());
			jgen.writeStringField("game", table.getGame());
			
			provider.defaultSerializeField("board", table.board, jgen);
			
			jgen.writeArrayFieldStart("commands");
			if(table.getCommands() != null) for(Command command : table.getCommands()) {
				jgen.writeStartObject();
				jgen.writeNumberField("id", command.getId());
				jgen.writeStringField("command", command.getCommand());
				jgen.writeEndObject();
			}
			jgen.writeEndArray();
			
			jgen.writeEndObject();
		}
	}
	
	
	public static Model.Finder<Integer, Table> finder = new Model.Finder<Integer, Table>(
			Integer.class, Table.class);

	@Id
	private Integer id;

	@Required
	private String name;

	private Integer seed;
	
	private String game;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JsonManagedReference
	private List<Command> commands;
	
	@Transient
	private Board board;

	@PostConstruct
	public void init() {
		//board = (Board)Class.forName("game."+game+".Board").newInstance();
		//board.seedRandom(seed);
		//doesn't seem to want to work here?
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeed() {
		return seed;
	}

	public void setSeed(Integer seed) {
		this.seed = seed;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public List<Command> getCommands() {
		return commands;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	
}
