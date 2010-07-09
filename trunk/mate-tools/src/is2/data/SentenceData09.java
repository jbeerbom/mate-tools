package is2.data;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SentenceData09 {

	public FV fv;
	public FV m_fvs[];
	public String actParseTree;

	public String[] forms;

	// predicted lemma
	public String[] lemmas;

	public String[] org_lemmas;

	public int[] heads;
	public int[] phead;

	public String[] labels;

	// predicted edge name
	public String[] pedge;

	public String[] ppos;

	public String feats[][];
	

	public void setPPos(String[] pos) {
		ppos=pos;
	}
	
	public void setLemmas(String[] lemmas) {
		this.lemmas=lemmas;
	}

	public void setFeats(String[] fts) {
		feats = new String[fts.length][];
		for(int i=0;i<fts.length;i++) {
			feats[i] = fts[i].split("\\|");
		}
		pfeats =fts;
		
	}
	
	// gold part of speech
	public String[] gpos;
	
	public String[] split_lemma;

	public String[] sem;
	public int[] semposition;
	
	// predicate number, argument number -> argument string
	public String[][] arg;
	public int[][] argposition;

	public String[] fillp;

	public String[] ofeats;
	public String[] pfeats;

	public SentenceData09() {}

	public SentenceData09(String[] forms, String[] postags, String[] labs, int[] heads) {
		this.forms = forms;
		gpos = postags;
		labels = labs;
		this.heads = heads;
	}

	public SentenceData09(String[] forms, String[] lemmas, String[] postags, String[] labs, int[] heads) {
		this.forms = forms;
		gpos = postags;
		//ppos = postags;

		labels = labs;
		this.heads = heads;
		this.lemmas = lemmas;
	}
	public SentenceData09(String[] forms, String[] lemmas, String[] gpos, String[] ppos, String[] labs, int[] heads) {
		this.forms = forms;
		this.gpos = gpos;
		this.ppos = ppos;

		labels = labs;
		this.heads = heads;
		this.lemmas = lemmas;
		
	
	}
	public SentenceData09(String[] forms, String[] lemmas, String[] gpos, String[] ppos, String[] labs, int[] heads, String[] fillpred) {
		this.forms = forms;
		this.gpos = gpos;
		this.ppos = ppos;

		labels = labs;
		this.heads = heads;
		this.lemmas = lemmas;
		
		fillp =fillpred;
	}

	public SentenceData09(String[] forms, String[] lemmas, String[] olemmas,String[] gpos, String[] ppos, String[] labs, int[] heads, String[] fillpred) {
		this.forms = forms;
		this.gpos = gpos;
		this.ppos = ppos;

		labels = labs;
		this.heads = heads;
		this.lemmas = lemmas;
		this.org_lemmas =olemmas;
		fillp =fillpred;
	}

	public SentenceData09(String[] forms, String[] lemmas, String[] olemmas,String[] gpos, 
			String[] ppos, String[] labs, int[] heads, String[] fillpred, String[] of, String[] pf) {
		this.forms = forms;
		this.gpos = gpos;
		this.ppos = ppos;

		labels = labs;
		this.heads = heads;
	//	this.pheads = heads;
		this.phead =heads;
		this.pedge=labs;
		this.lemmas = lemmas;
		this.org_lemmas =olemmas;
		
		this.ofeats =of;
		this.pfeats =pf;
		fillp =fillpred;
	}

	
	
	
	public void setFeatureVector (FV fv, int w) {
		if (m_fvs==null) m_fvs = new FV[length()];
		m_fvs[w]= fv;
	}


	public int length () {
		return forms.length;
	}

	@Override
	public String toString () {
		StringBuffer sb = new StringBuffer();
		for(int k=0;k<forms.length;k++) sb.append(k+1).append('\t').append(forms[k]).append('\t').append(heads[k]).append('\t').append(labels[k]).append('\n');
		return sb.toString();
	}

  
		final public void write (DataOutputStream out) throws IOException {
		
		out.writeInt(forms.length);
		for(int k=0;k<forms.length;k++) {
			out.writeUTF(forms[k]);
			out.writeUTF(ppos[k]);
			out.writeUTF(gpos[k]);
			out.writeInt(heads[k]);
			out.writeUTF(labels[k]);
			out.writeUTF(org_lemmas[k]);
			out.writeUTF(lemmas[k]);
			out.writeUTF(ofeats[k]);  // needed for mtag
			out.writeUTF(fillp[k]);
		}
	
	//	out.writeUTF(actParseTree);
	
	}

	final public void read (DataInputStream dis) throws IOException {
		
		int l = dis.readInt();
		
		forms = new String[l];
		org_lemmas = new String[l];
		lemmas = new String[l];
		ppos = new String[l];
		gpos = new String[l];
		labels = new String[l];
		heads = new int[l];
		fillp = new String[l];
		ofeats=new String[l];
				
		for(int k=0;k<l;k++) {
			forms[k] = dis.readUTF();
			ppos[k]=dis.readUTF();
			gpos[k]=dis.readUTF();
			heads[k]=dis.readInt();
			labels[k]=dis.readUTF();
			org_lemmas[k]=dis.readUTF();
			lemmas[k]=dis.readUTF();
			ofeats[k]=dis.readUTF();
			fillp[k]=dis.readUTF();
					
		}
	}
	

	private void readObject (ObjectInputStream in) throws IOException, ClassNotFoundException {
		forms = (String[])in.readObject();
		lemmas = (String[])in.readObject();
		ppos = (String[])in.readObject();
		heads = (int[])in.readObject();
		labels = (String[])in.readObject();
	}

	public void addPredicate(int i, String s) {
		
		int predId;
		if (sem == null) {
			predId=0;
			sem = new String[1];
			semposition = new int[1];
		}
		else  {
			predId=sem.length;
			String p[] = new String[sem.length+1];
			System.arraycopy(sem, 0, p, 0, sem.length);
			int id[] = new int[sem.length+1];
			System.arraycopy(semposition, 0, id, 0, semposition.length);
			sem =p;
			semposition=id;
		}
		sem[predId]=s;
		semposition[predId]=i;
	}

	
	/**
	 * Add an argument 
	 * @param i the instance (the child)
	 * @param predId the id of the predicate (the head)
	 * @param a the label of the argument
	 */
	public void addArgument(int i, int predId, String a) {
		
		if (a ==null || a.equals("_")) return;
		
		// ensure the space for the argument in the data structure
		if (arg == null) {
			arg = new String[predId+1][];
			argposition = new int[predId+1][];
		} else if (arg.length<=predId) {
			String p[][] = new String[predId+1][];
			System.arraycopy(arg, 0, p, 0, arg.length);
			arg =p;

			int id[][] = new int[predId+1][];
			System.arraycopy(argposition, 0, id, 0, argposition.length);
			argposition = id;
		}
		
		
		
		int aId;
		if (arg[predId]==null) {
			aId=0;
			arg[predId] = new String[1];
			argposition[predId] = new int[1];
		} else {
			aId =arg[predId].length;
			String args[] = new String[arg[predId].length+1];
			System.arraycopy(arg[predId], 0, args, 0, arg[predId].length);
			arg[predId]=args;
			
			int argsId[] = new int[argposition[predId].length+1];
			System.arraycopy(argposition[predId], 0, argsId, 0, argposition[predId].length);
			argposition[predId]=argsId;
		}
		
		arg[predId][aId]=a;
		argposition[predId][aId]=i;
		
	}
	
	public int[] getParents() {
		return heads;
	}

	public String[] getLabels() {
		return labels;
	}

	public String printSem() {
		
		if (sem==null) return "";
		StringBuilder s = new StringBuilder();
		
		for(int k=0;k<sem.length;k++) {
			s.append(sem[k]).append("\n");
			
			if (arg==null) {
				s.append("arg == null");
			}else
			if (arg.length<=k) {
				s.append("args.length <=k arg.length:"+arg.length+" k:"+k);
			} else if (arg[k]!=null) {
			for(int a=0;a< arg[k].length;a++) {
				s.append("  ").append(arg[k][a]);
			}
			} else {
				s.append("args == null ");
			}
			s.append('\n');
		}
		return s.toString();
	}

	
	/**
	 * Initialize a instance so that a tagger, parser, etc. could be applied
	 * @param forms
	 */
	public void init(String[] forms) {
		this.forms = forms;
		heads = new int[forms.length];
		gpos = new String[forms.length];
		ppos = new String[forms.length];
		lemmas = new String[forms.length];
		feats = new String[forms.length][0];
		labels = new String[forms.length];
		
		
	}

	
	
}
