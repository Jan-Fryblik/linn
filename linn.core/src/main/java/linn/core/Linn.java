/**
 * Copyright (c) 2016 by Thomas Trojer <thomas@trojer.net>
 * LINN - A small L-System interpreter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package linn.core;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import static com.google.common.base.Preconditions.*;

import linn.core.lang.production.Production;

public class Linn implements RuleProductionContainer {
	// meta information
	private String name;
	private String author;
	private Date date;
	// settings
	private double defaultMoveLength = 1.0;
	private double defaultYawAngle = Math.PI * 0.5f;
	private double defaultPitchAngle = Math.PI * 0.5f;
	private double defaultRollAngle = Math.PI * 0.5f;
	// rules
	final Map<String, List<Integer>> ruleIdsOfRuleName = Maps.newHashMap();
	final Map<Integer, Double> weightOfRuleId = Maps.newHashMap();
	// rule content
	final Map<Integer, List<Production>> productionsOfRuleId = Maps
			.newHashMap();

	public Linn() {
		this.setDate(new Date());
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		checkNotNull(name);
		this.name = name;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(final String author) {
		checkNotNull(author);
		this.author = author;
	}

	public Date getDate() {
		return this.date;
	}

	protected void setDate(final Date date) {
		checkNotNull(date);
		this.date = date;
	}

	public void setDefaultMoveLength(double moveLength) {
		checkArgument(moveLength > 0);
		this.defaultMoveLength = moveLength;
	}

	public void setDefaultYawAngle(double yawAngle) {
		this.defaultYawAngle = yawAngle;
	}

	public double getDefaultYawAngle() {
		return this.defaultYawAngle;
	}

	public void setDefaultPitchAngle(double pitchAngle) {
		this.defaultPitchAngle = pitchAngle;
	}

	public double getDefaultPitchAngle() {
		return this.defaultPitchAngle;
	}

	public void setDefaultRollAngle(double rollAngle) {
		this.defaultRollAngle = rollAngle;
	}

	public double getDefaultRollAngle() {
		return this.defaultRollAngle;
	}

	public double getDefaultMoveLength() {
		return this.defaultMoveLength;
	}

	public void addRule(final Integer ruleId, final String ruleName) {
		checkNotNull(ruleId);
		checkNotNull(ruleName);
		this.ruleIdsOfRuleName.putIfAbsent(ruleName,
				Lists.<Integer> newArrayList());
		final List<Integer> ruleIds = this.ruleIdsOfRuleName.get(ruleName);
		ruleIds.add(ruleId);
		// set default weight, if none is set yet
		this.weightOfRuleId.putIfAbsent(ruleId, 1.0);
		// create empty list of productions for the rule
		this.productionsOfRuleId.put(ruleId, Lists.<Production> newArrayList());
	}

	@Override
	public void addRuleProduction(final Integer ruleId,
			final Production production) {
		checkNotNull(production);
		checkArgument(this.productionsOfRuleId.containsKey(ruleId));
		this.productionsOfRuleId.putIfAbsent(ruleId,
				Lists.<Production> newArrayList());
		final List<Production> ruleProductions = this.productionsOfRuleId
				.get(ruleId);
		// add as last production
		ruleProductions.add(production);
	}

	@Override
	public List<Production> getRuleProductions(final Integer ruleId) {
		checkNotNull(ruleId);
		checkArgument(this.productionsOfRuleId.containsKey(ruleId));
		return Collections.unmodifiableList(this.productionsOfRuleId
				.get(ruleId));
	}

	public List<Integer> getRuleIds(final String ruleName) {
		checkNotNull(ruleName);
		return Collections.unmodifiableList(this.ruleIdsOfRuleName
				.get(ruleName));
	}

	public void setRuleWeight(final Integer ruleId, final double weight) {
		checkNotNull(ruleId);
		checkArgument(weight >= 0);
		this.weightOfRuleId.put(ruleId, weight);
	}

	public double getRuleWeight(final Integer ruleId) {
		checkNotNull(ruleId);
		checkArgument(this.weightOfRuleId.containsKey(ruleId));
		return this.weightOfRuleId.get(ruleId);
	}

	// init
	// hooks/handlers
	// production
	// production listeners

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("linn '" + this.name + "' (");
		if (this.author != null) {
			sb.append(this.author + ", ");
		}
		sb.append(this.date + ") {\n");
		for (final Entry<String, List<Integer>> ruleEntry : this.ruleIdsOfRuleName
				.entrySet()) {
			final String ruleName = ruleEntry.getKey();
			for (final Integer ruleId : ruleEntry.getValue()) {
				sb.append("\t" + ruleName + " --"
						+ this.weightOfRuleId.get(ruleId) + "-> ");
				for (final Production prod : this.productionsOfRuleId
						.get(ruleId)) {
					sb.append(prod.getName() + " ");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(";\n");
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
